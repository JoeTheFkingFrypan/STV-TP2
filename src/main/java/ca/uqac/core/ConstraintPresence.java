package ca.uqac.core;

public class ConstraintPresence extends Constraint {

    public ConstraintPresence(final String key) {
        super(key);
    }

    @Override
    public String toString() {
        return this.key;
    }
}
