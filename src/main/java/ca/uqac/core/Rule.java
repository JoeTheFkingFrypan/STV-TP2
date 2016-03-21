package ca.uqac.core;

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

    public boolean validateQuery(final String query) {
        return false;
    }

    @Override
    public String toString() {
        return "[RULE] " + StringUtils.join(this.constraintList, " & ");
    }
}
