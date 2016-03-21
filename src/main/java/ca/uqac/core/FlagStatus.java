package ca.uqac.core;

public enum FlagStatus {
    FLAG_PRESENT("FLAG_PRESENT"),
    FLAG_ABSENT("FLAG_ABSENT");

    private String label;

    FlagStatus(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
