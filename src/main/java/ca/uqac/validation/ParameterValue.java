package ca.uqac.validation;

import java.util.Objects;

class ParameterValue {
    private final String key;
    private final String value;

    ParameterValue(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    String getKey() {
        return this.key;
    }

    String getValue() {
        return this.key;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!ParameterValue.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final ParameterValue other = (ParameterValue) obj;
        return this.key.equals(other.key) && this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

    @Override
    public String toString() {
        return this.key + " = " + this.value;
    }
}
