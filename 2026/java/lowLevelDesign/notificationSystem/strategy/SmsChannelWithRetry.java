package lowLevelDesign.notificationSystem.strategy;

import lowLevelDesign.notificationSystem.domain.NotificationChannel;
import lowLevelDesign.notificationSystem.domain.NotificationRequest;
import lowLevelDesign.notificationSystem.domain.NotificationType;
import lowLevelDesign.notificationSystem.domain.RetryPolicy;

public class SmsChannelWithRetry implements NotificationChannel {
    private final RetryMechanism retryMechanism;
    private int internalSimulatedFailureCounter = 0; // Simulated mock state tracker

    public SmsChannelWithRetry() {
        // Configure default policy limits: Retry up to 3 times, start with a 100ms wait, multiply by 2x each time
        RetryPolicy defaultPolicy = new RetryPolicy(3, 100, 2.0);
        this.retryMechanism = new RetryMechanism(defaultPolicy);
    }

    @Override
    public void send(NotificationRequest request) {
        // Wrap the execution code inside our functional retry engine wrapper
        retryMechanism.execute(() -> {
            // Simulate transient gateway instability
            if (internalSimulatedFailureCounter < 2) {
                internalSimulatedFailureCounter++;
                throw new RuntimeException("SMS Gateway Gateway timed out (Network Jitter Exception).");
            }

            // This prints only when the retry mechanism succeeds
            System.out.println(String.format("[SMS CHANNEL SUCCESS] Text message delivered to %s: \"%s\"",
                    request.getRecipientId(), request.getMessage()));
        });
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.SMS;
    }
}
