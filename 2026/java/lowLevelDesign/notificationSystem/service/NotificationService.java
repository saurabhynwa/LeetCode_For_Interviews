package lowLevelDesign.notificationSystem.service;

import lowLevelDesign.notificationSystem.domain.NotificationChannel;
import lowLevelDesign.notificationSystem.domain.NotificationRequest;
import lowLevelDesign.notificationSystem.strategy.NotificationChannelRegistry;

/**
 * Centralized, high-level Facade API. Follows Single Responsibility Principles.
 */
public class NotificationService {
    private final NotificationChannelRegistry registry;

    public NotificationService(NotificationChannelRegistry registry) {
        this.registry = registry;
    }

    /**
     * Accepts a generic notification request and dynamically routes it to the target strategy channel.
     */
    public void sendNotification(NotificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("System Guardrail Violation: Notification request payload cannot be null.");
        }

        // Dynamically resolve the correct routing strategy channel at runtime
        NotificationChannel targetedChannel = registry.getSupportedChannel(request.getNotificationType());
        targetedChannel.send(request);
    }
}
