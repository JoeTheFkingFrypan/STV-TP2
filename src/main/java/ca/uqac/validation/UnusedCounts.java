package ca.uqac.validation;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class UnusedCounts {
    private final ConcurrentMap<ParameterValue, AtomicLong> unusedCounts;

    public UnusedCounts() {
        unusedCounts = new ConcurrentHashMap<>();
    }

    public void increment(final ParameterValue lhs, ParameterValue rhs) {
        unusedCounts.putIfAbsent(lhs, new AtomicLong(0));
        unusedCounts.get(lhs).incrementAndGet();

        unusedCounts.putIfAbsent(rhs, new AtomicLong(0));
        unusedCounts.get(rhs).incrementAndGet();
    }

    public int getWeightFrom(ParameterValue lhs, ParameterValue rhs) {
        return this.unusedCounts.get(lhs).intValue() + this.unusedCounts.get(rhs).intValue();
    }

    @Override
    public String toString() {
        return StringUtils.join(this.unusedCounts, "\n");
    }
}
