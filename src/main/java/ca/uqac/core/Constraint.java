package ca.uqac.core;

abstract class Constraint {

    final String key;

    Constraint(final String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
