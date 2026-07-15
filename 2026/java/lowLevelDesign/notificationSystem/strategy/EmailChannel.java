package lowLevelDesign.notificationSystem.strategy;

import lowLevelDesign.notificationSystem.domain.NotificationChannel;
import lowLevelDesign.notificationSystem.domain.NotificationRequest;
import lowLevelDesign.notificationSystem.domain.NotificationType;

public class EmailChannel implements NotificationChannel {

    @Override
    public void send(NotificationRequest notificationRequest) {
        System.out.println("Sending notification email to " + notificationRequest.getRecipientId() + " with message = "
                + notificationRequest.getMessage());
    }

    @Override
    public boolean supports(NotificationType notificationType) {
        return notificationType == NotificationType.EMAIL;
    }
}
