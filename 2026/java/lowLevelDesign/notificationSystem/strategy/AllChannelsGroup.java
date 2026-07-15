package lowLevelDesign.notificationSystem.strategy;

import lowLevelDesign.notificationSystem.domain.NotificationChannel;
import lowLevelDesign.notificationSystem.domain.NotificationRequest;
import lowLevelDesign.notificationSystem.domain.NotificationType;

import java.util.ArrayList;
import java.util.List;

/**
 * 🌟 COMPOSITE PATTERN WIN: Behaves like a single channel externally,
 * but routes the payload to all inner registered channels internally.
 */
public class AllChannelsGroup implements NotificationChannel {
    private final List<NotificationChannel> registeredSubChannels;

    public AllChannelsGroup() {
        this.registeredSubChannels = new ArrayList<>();
        // Inject our specific delivery strategy vectors
        this.registeredSubChannels.add(new EmailChannel());
        this.registeredSubChannels.add(new SmsChannel());
        this.registeredSubChannels.add(new PushChannel());
    }

    @Override
    public void send(NotificationRequest request) {
        System.out.println("📢 [BROADCAST ROUTER] Multi-channel notification triggered. Fan-out sequence initiated...");

        // Loop over each independent sub-channel and execute delivery
        for (NotificationChannel channel : registeredSubChannels) {
            try {
                channel.send(request);
            } catch (Exception errorException) {
                // Error Isolation: One gateway failing must NOT stop other channels from delivering!
                System.err.println("⚠️ Broadcast channel dispatch segment failed: " + errorException.getMessage());
            }
        }
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.ALL;
    }
}