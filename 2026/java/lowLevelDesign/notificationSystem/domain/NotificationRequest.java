package lowLevelDesign.notificationSystem.domain;

public class NotificationRequest {
    private final String recipientId;
    private final String message;
    private final NotificationType notificationType;

    public NotificationRequest(String recipientId, String message, NotificationType notificationType){
        this.recipientId = recipientId;
        this.message = message;
        this.notificationType = notificationType;
    }

    // accessors
    public String getRecipientId(){
        return recipientId;
    }

    public String getMessage(){return message;}

    public NotificationType getNotificationType(){return notificationType;}
}
