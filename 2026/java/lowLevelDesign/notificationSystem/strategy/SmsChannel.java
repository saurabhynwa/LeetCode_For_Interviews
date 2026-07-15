package lowLevelDesign.notificationSystem.strategy;

import lowLevelDesign.notificationSystem.domain.NotificationChannel;
import lowLevelDesign.notificationSystem.domain.NotificationRequest;
import lowLevelDesign.notificationSystem.domain.NotificationType;

public class SmsChannel implements NotificationChannel {
    @Override
    public void send(NotificationRequest request) {
        // In production, this encapsulates a Twilio or Infobip gateway API client
        System.out.println(String.format("💬 [SMS CHANNEL] Dispatching text to %s: \"%s\"",
                request.getRecipientId(), request.getMessage()));
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.SMS;
    }
}
