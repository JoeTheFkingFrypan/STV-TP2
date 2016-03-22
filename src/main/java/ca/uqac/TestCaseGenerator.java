package ca.uqac;

import ca.uqac.validation.Pair;
import ca.uqac.validation.UnusedCounts;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TestCaseGenerator {

    private final Context context;
    private final List<Pair> testCases;
    private List<Pair> unusedPairs;
    private List<Pair> availablePairs;
    private UnusedCounts unusedCounts;

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

    private void computeTestCases() {

        /*List<String> parameterPositions = this.context.getParameterPositions();
        for (int i = 0; i < parameterPositions.size(); ++i) {
            System.out.println("POSITION [" + i + "] " + parameterPositions.get(i));
        }

        List<String> ordering = this.context.getParameterOrdering();
        for (int i = 0; i < ordering.size(); ++i) {
            System.out.println("ORDERING [" + i + "] " + parameterPositions.get(i));
        }*/

        this.unusedCounts = new UnusedCounts();
        for(Pair p: this.availablePairs) {
            this.unusedCounts.increment(p.getLhsParameterValue(), p.getRhsParameterValue());
        }

        Pair best = this.findBestPair();
        this.testCases.add(best);
        System.out.println("[DEBUG] Best pair should be: " + best);

        /*int firstPos = parameterPositions.indexOf(best.getLhsParameterKey()); // position of first value from best unused pair
        int secondPos = parameterPositions.indexOf(best.getRhsParameterKey()); // position of first value from best unused pair
        Collections.swap(ordering, firstPos, secondPos);
        for (int i = 0; i < ordering.size(); ++i) {
            System.out.println("NEW ORDERING [" + i + "] " + parameterPositions.get(i));
        }

        for(int i=2; i<this.context.getNumberOfParameters(); i++) {

        }*/


    }

    private Pair findBestPair() {
        // pick "best" unusedPair -- the pair which has the sum of the most unused values
        int bestWeight = 0;
        int indexOfBestPair = 0;
        for (int i = 0; i < this.unusedPairs.size(); ++i) {
            Pair p = this.unusedPairs.get(i);
            int weight = this.unusedCounts.getWeightFrom(p.getLhsParameterValue(), p.getRhsParameterValue());
            //System.out.println("[DEBUG]     Weight for " + i + "th(" + p +") is " + weight);
            if (weight > bestWeight) {
                bestWeight = weight;
                indexOfBestPair = i;
            }
        }
        System.out.println("[DEBUG] Index from best pair: " + indexOfBestPair);
        return this.unusedPairs.get(indexOfBestPair);
    }

    void printSuggestedTestCases() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "\n============== TEST CASES ==============\n" +
                StringUtils.join(this.testCases, "\n");
    }

}
