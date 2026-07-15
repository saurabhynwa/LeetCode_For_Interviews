package lowLevelDesign.notificationSystem.strategy;

import lowLevelDesign.notificationSystem.domain.NotificationChannel;
import lowLevelDesign.notificationSystem.domain.NotificationRequest;
import lowLevelDesign.notificationSystem.domain.NotificationType;

public class PushChannel implements NotificationChannel {
    @Override
    public void send(NotificationRequest request) {
        // In production, this dispatches via Google Firebase (FCM) or Apple Push (APNs)
        System.out.println(String.format("📱 [PUSH CHANNEL] Firing device alert to token %s: \"%s\"",
                request.getRecipientId(), request.getMessage()));
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.PUSH;
    }
}
