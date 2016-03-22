package ca.uqac.validation;

public class Pair {
    private final ParameterValue lhs;
    private final ParameterValue rhs;

    public Pair(final String lhsKey, final String lhsValue, final String rhsKey, final String rhsValue) {
        this.lhs = new ParameterValue(lhsKey, lhsValue);
        this.rhs = new ParameterValue(rhsKey, rhsValue);
    }

    @Override
    public String toString() {
        return this.lhs.getKey() + "(" + this.lhs.getValue() + ") & " + this.rhs.getKey() + "(" + this.rhs.getValue() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Pair.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Pair other = (Pair) obj;
        return this.lhs.equals(other.lhs) && this.rhs.equals(other.rhs);
    }

    public ParameterValue getLhsParameterValue() {
        return this.lhs;
    }

    public ParameterValue getRhsParameterValue() {
        return this.rhs;
    }
}
