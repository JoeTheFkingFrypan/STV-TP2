package ca.uqac.validation;

import java.util.HashMap;
import java.util.Map;

public class ParameterPosition {
    private final Map<Integer, ParameterValue> positions;

    public ParameterPosition() {
        this.positions = new HashMap<>();
    }

    public void add(final int position, final ParameterValue parameterValue) {
        this.positions.put(position, parameterValue);
    }
}
