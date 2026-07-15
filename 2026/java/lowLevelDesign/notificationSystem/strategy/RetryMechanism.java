package lowLevelDesign.notificationSystem.strategy;

import lowLevelDesign.notificationSystem.domain.RetryPolicy;

/**
 * Encapsulates the algorithmic implementation of exponential backoff loops.
 */
public class RetryMechanism {
    private final RetryPolicy policy;

    public RetryMechanism(RetryPolicy policy) {
        this.policy = policy;
    }

    /**
     * Executes a functional interface command block with stateful retry capabilities.
     */
    public void execute(Runnable taskCommand) {
        int attempts = 0;
        long currentDelay = policy.getInitialDelayMillis();

        while (attempts < policy.getMaxAttempts()) {
            try {
                attempts++;
                taskCommand.run(); // Try executing the channel dispatch action
                return;            // Success! Break out of the execution loop instantly.
            } catch (Exception transientException) {
                System.err.println(String.format("Attempt %d of %d failed: %s",
                        attempts, policy.getMaxAttempts(), transientException.getMessage()));

                if (attempts >= policy.getMaxAttempts()) {
                    // Maximum attempts exhausted. Escalate the error up to the handler
                    throw new RuntimeException("Maximum notification delivery attempts exhausted. Dispatch failed " +
                            "permanent.", transientException);
                }

                // Calculate exponential backoff interval
                currentDelay = (long) (currentDelay * policy.getMultiplier());

                // Inject Jitter (Random noise to prevent thundering herd problems on third party gateways)
                long jitter = (long) (Math.random() * (currentDelay * 0.1));
                long finalSleepTime = currentDelay + jitter;

                try {
                    System.out.println(String.format("⏳ Backing off. Sleeping for %d ms before next attempt...",
                            finalSleepTime));
                    Thread.sleep(finalSleepTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry wait cycle interrupted abruptly.", ie);
                }
            }
        }
    }
}

