package ca.uqac;

import ca.uqac.core.Option;
import ca.uqac.validation.Pair;
import ca.uqac.validation.ParameterValue;
import ca.uqac.validation.UnusedCounts;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class TestCaseGenerator {

    private final int poolSize = 20; //TODO get it as a parameter
    private final Context context;
    private final List<String[]> testCases;
    private List<Pair> unusedPairs;
    private List<Pair> availablePairs;
    private UnusedCounts unusedCounts = new UnusedCounts();

    TestCaseGenerator(final Context context) {
        this.testCases = new ArrayList<>();
        this.context = context;
        context.displayInfo();
        this.processPairs();
        this.computeTestCases();
    }

    private void processPairs() {
        System.out.println("\n============== PAIRS ==============");
        this.availablePairs = this.context.getPairs();
        this.unusedPairs = new ArrayList<>(this.availablePairs);
        System.out.println("- There are " + this.availablePairs.size() + " pairs that complies to rules");
    }

    private int NumberPairsCaptured(String[] testSet)
    {
        int count = 0;
        List<String> parameterOrdering = context.getParameterOrdering();

        for (int i = 0; i < testSet.length - 1; ++i)
        {
            for (int j = i + 1; j < testSet.length; ++j)
            {
                Pair p = new Pair( parameterOrdering.get(i), testSet[i], parameterOrdering.get(j), testSet[j]);
                if (unusedPairs.contains(p))
                    ++count;
            }
        }
        return count;
    }

    private void computeTestCases() {
        System.out.println("\nComputing testsets which capture all possible pairs...");
        int numberParameters = context.getNumberOfParameters();
        List<String> parameterOrdering = context.getParameterOrdering();

        //count unused
        for(Pair p : availablePairs)
        {
            unusedCounts.increment(p.getLhsParameterValue(), p.getRhsParameterValue());
        }

        while(unusedPairs.size() > 0)
        {
            // holds candidate testSets
            String[][] candidateSets = new String[poolSize][];

            for (int candidate = 0; candidate < poolSize; ++candidate) {
                String[] testSet = generateTestSet();
                candidateSets[candidate] = testSet;  // add candidate testSet to candidateSets array
            }

            // Iterate through candidateSets to determine the best candidate
            int indexOfBestCandidate = 0;
            int mostPairsCaptured = NumberPairsCaptured(candidateSets[indexOfBestCandidate]);

            for (int i = 1; i < candidateSets.length; ++i)
            {
                int pairsCaptured = NumberPairsCaptured(candidateSets[i]);
                if (pairsCaptured > mostPairsCaptured)
                {
                    mostPairsCaptured = pairsCaptured;
                    indexOfBestCandidate = i;
                }
            }

            String[] bestTestSet = candidateSets[indexOfBestCandidate];
            testCases.add(bestTestSet); // Add the best candidate to the main testCases List

            // now perform all updates
            for (int i = 0; i <= numberParameters - 2; ++i)
            {
                for (int j = i + 1; j <= numberParameters - 1; ++j)
                {
                    String k1 = parameterOrdering.get(i);
                    String v1 = bestTestSet[i]; // value 1 of newly added pair
                    String k2 = parameterOrdering.get(j);
                    String v2 = bestTestSet[j]; // value 2 of newly added pair

                    ParameterValue p1 = new ParameterValue(k1, v1);
                    ParameterValue p2 = new ParameterValue(k2, v2);

                    Pair p = new Pair(k1, v1, k2, v2);

                    //ConsoleWriteLine("Decrementing the unused counts for " + v1 + " and " + v2);
                    unusedCounts.decrement(p1, p2);
                    unusedPairs.remove(p);
                }
            }
        }
    }

    private String[] generateTestSet()
    {
        // pick "best" unusedPair -- the pair which has the sum of the most unused values
        Pair best = findBestPair();
        return getTestSet(best);
    }

    private Pair findBestPair() {
        int bestWeight = 0;
        int indexOfBestPair = 0;
        for (int i = 0; i < unusedPairs.size(); ++i) {
            Pair curr = unusedPairs.get(i);
            int weight = unusedCounts.getWeightFrom(curr.getLhsParameterValue(), curr.getRhsParameterValue());
            if (weight > bestWeight) {
                bestWeight = weight;
                indexOfBestPair = i;
            }
        }

        return unusedPairs.get(indexOfBestPair);
    }

    private String[] getTestSet(Pair best)
    {
        // make an empty candidate testSet
        String[] testSet = new String[ context.getNumberOfParameters() ];

        // position of values from best unused pair
        int firstPos = context.getParameterOrdering().indexOf(best.getLhsParameterValue().getKey());
        int secondPos = context.getParameterOrdering().indexOf(best.getRhsParameterValue().getKey());
        boolean startOver = true;

        while(startOver) {
            startOver = false;

            // generate a random order to fill parameter positions
            int[] ordering = new int[ context.getNumberOfParameters() ];
            for (int i = 0; i <  context.getNumberOfParameters() ; ++i) // initially all in order
                ordering[i] = i;

            // put firstPos at ordering[0] && secondPos at ordering[1]
            ordering[0] = firstPos;
            ordering[firstPos] = 0;

            int t = ordering[1];
            ordering[1] = secondPos;
            ordering[secondPos] = t;

            // shuffle ordering[2] thru ordering[last]
            for (int i = 2; i < ordering.length; i++)  // Knuth shuffle. start at i=2 because want first two slots left alone
            {
                int j = ThreadLocalRandom.current().nextInt(i, ordering.length);
                int temp = ordering[j];
                ordering[j] = ordering[i];
                ordering[i] = temp;
            }

            // place two parameter values from best unused pair into candidate testSet
            testSet[firstPos] = best.getLhsParameterValue().getValue();
            testSet[secondPos] = best.getRhsParameterValue().getValue();

            List<Option> options = context.getOptions();

            // for remaining parameter positions in candidate testSet, try each possible legal value, picking the one which captures the most unused pairs . . .
            for (int i = 2; i <  context.getNumberOfParameters() ; ++i) // start at 2 because first two parameter have been placed
            {
                int currPos = ordering[i];

                //get possibles values for current parameter
                List<String> temp = options.get(currPos).getPossibleValues();
                String[] possibleValues = new String[temp.size()];
                possibleValues = temp.toArray(possibleValues);

                int currentCount = 0;  // count the unusedPairs grabbed by adding a possible value
                int highestCount = -1;  // highest of these counts
                int bestJ = 0;         // index of the possible value which yields the highestCount
                for (int j = 0; j < possibleValues.length; ++j) // examine pairs created by each possible value and each parameter value already there
                {
                    String firstKey = context.getParameterOrdering().get(ordering[i]);
                    String firstValue = possibleValues[j];
                    boolean disgardValue = false;
                    currentCount = 0;

                    for (int p = 0; p < i; ++p)  // parameters already placed
                    {
                        String secondKey = context.getParameterOrdering().get(ordering[p]);
                        String secondValue = testSet[ordering[p]];

                        Pair reverseCandidatePair = new Pair(firstKey, firstValue, secondKey, secondValue);
                        Pair candidatePair = new Pair(secondKey, secondValue, firstKey, firstValue);

                        if (!disgardValue) {
                            //check if this pair is valid || reverseCandidatePair is checked inside pairNotForbidden implementation
                            if (!context.pairNotForbidden(candidatePair.getLhsParameterValue(), candidatePair.getRhsParameterValue())) {
                                disgardValue = true;
                                currentCount = -1;
                            }

                            if (unusedPairs.contains(candidatePair) ||
                                    unusedPairs.contains(reverseCandidatePair))  // because of the random order of positions, must check both possibilities
                            {
                                //ConsoleWriteLine("Found " + candidatePair[0] + "," + candidatePair[1] + " in unusedPairs");
                                ++currentCount;
                            }
                        }
                    } // p -- each previously placed parameter
                    if (currentCount > highestCount) {
                        highestCount = currentCount;
                        bestJ = j;
                    }

                } // j -- each possible value at currPos
                //ConsoleWriteLine("The best value is " + possibleValues[bestJ] + " with count = " + highestCount);

                if (bestJ < 0) {
                    startOver = true;
                    i =  context.getNumberOfParameters()  + 1; //Exit loop
                } else
                    testSet[currPos] = possibleValues[bestJ]; // place the value which captured the most pairs
            } // i -- each testSet position
        }

        return testSet;
    }

    void printSuggestedTestCases() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {

        String res = "\n============== TEST CASES ==============\n";
        for(String[] testSet : testCases)
        {
            res += Arrays.toString(testSet) + "\n";
        }
        return  res;
    }

}
