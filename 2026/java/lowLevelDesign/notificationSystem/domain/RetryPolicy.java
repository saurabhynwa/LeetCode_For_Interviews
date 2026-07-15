package lowLevelDesign.notificationSystem.domain;

/**
 * Defines the operational boundaries for transient error recovery loops.
 */
public class RetryPolicy {
    private final int maxAttempts;
    private final long initialDelayMillis;
    private final double multiplier;

    public RetryPolicy(int maxAttempts, long initialDelayMillis, double multiplier) {
        this.maxAttempts = maxAttempts;
        this.initialDelayMillis = initialDelayMillis;
        this.multiplier = multiplier;
    }

    public int getMaxAttempts() { return maxAttempts; }
    public long getInitialDelayMillis() { return initialDelayMillis; }
    public double getMultiplier() { return multiplier; }
}
