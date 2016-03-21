package ca.uqac.core;

import ca.uqac.validation.ParameterValue;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private final List<Constraint> constraintList;

    public Rule() {
        this.constraintList = new ArrayList<>();
    }

    public void addConstraint(final Constraint c) {
        this.constraintList.add(c);
    }

    public boolean validateParameters(final ParameterValue lhs, final ParameterValue rhs) {
        Preconditions.checkState(this.forbidPair());

        Constraint c1 = this.constraintList.get(0);
        Constraint c2 = this.constraintList.get(1);

        return !( (c1.match(lhs) && c2.match(rhs)) || (c2.match(lhs) && c1.match(rhs)) );
    }

    @Override
    public String toString() {
        return "[RULE] " + StringUtils.join(this.constraintList, " & ");
    }

    public boolean forbidPair() {
        return this.constraintList.size() == 2;
    }
}
