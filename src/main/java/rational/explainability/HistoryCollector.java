package rational.explainability;

import rational.RationalState;
import rational.mapping.Believes;

public interface HistoryCollector {
    public void collectHistoryFromBeliefs(Believes beliefs);
    public void collectHistoryFromReasoning(RationalState state);
}
