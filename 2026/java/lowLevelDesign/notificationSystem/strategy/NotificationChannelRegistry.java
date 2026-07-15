package lowLevelDesign.notificationSystem.strategy;

import lowLevelDesign.notificationSystem.domain.NotificationChannel;
import lowLevelDesign.notificationSystem.domain.NotificationType;

import java.util.ArrayList;
import java.util.List;
// import java.util.concurrent.CopyOnWriteArrayList; // for thread-safe & concurrency

/**
 * Decouples routing registration entirely from the core service facade layer.
 */
public class NotificationChannelRegistry {
    private final List<NotificationChannel> channels;

    // CONCURRENCY WIN: CopyOnWriteArrayList allows thread-safe, lock-free reads while allowing safe concurrent channel
    // insertions at runtime.

    public NotificationChannelRegistry() {
        // this.channels = new CopyOnWriteArrayList<>(); // for thread-safe and concurrency
        this.channels = new ArrayList<>(); // remove this when switching to thread-safe and concurrency
        // Register default out-of-the-box infrastructure strategies
        registerChannel(new EmailChannel());
        registerChannel(new SmsChannel());
        registerChannel(new PushChannel());

        // 🌟 Simply append our new composite router block into our system inventory
        registerChannel(new AllChannelsGroup());
    }

    public void registerChannel(NotificationChannel channel) {
        this.channels.add(channel);
    }

    public NotificationChannel getSupportedChannel(NotificationType type) {
        for (NotificationChannel channel : channels) {
            if (channel.supports(type)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Unsupported notification type context channel: " + type);
    }
}
