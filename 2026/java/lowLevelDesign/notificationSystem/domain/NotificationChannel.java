package lowLevelDesign.notificationSystem.domain;

/**
 * Open-Closed Principle: Every notification channel must implement this contract.
 */
public interface NotificationChannel {
    // contract that every notification channel should implement
    void send(NotificationRequest notificationRequest);
    boolean supports(NotificationType notificationType);
}
