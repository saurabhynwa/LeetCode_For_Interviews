package lowLevelDesign.notificationSystem;

import lowLevelDesign.notificationSystem.domain.NotificationRequest;
import lowLevelDesign.notificationSystem.domain.NotificationType;
import lowLevelDesign.notificationSystem.service.NotificationService;
import lowLevelDesign.notificationSystem.strategy.NotificationChannelRegistry;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize our decoupled registry infrastructure
        NotificationChannelRegistry registry = new NotificationChannelRegistry();
        NotificationService notificationService = new NotificationService(registry); // remove this when working for
        // thread safe

        // 2. Dispatch a sequence of clean multi-channel domain requests
        NotificationRequest emailReq = new NotificationRequest(
                "saurabh.somani@example.com",
                "Your credit card application is approved!",
                NotificationType.EMAIL);

        NotificationRequest smsReq = new NotificationRequest(
                "+919876543210",
                "OTP for cash withdrawal transaction is 4561.",
                NotificationType.SMS);

        NotificationRequest pushReq = new NotificationRequest(
                "device_token_xyz_101",
                "Live tournament match starting now !",
                NotificationType.PUSH);

        notificationService.sendNotification(emailReq);
        notificationService.sendNotification(smsReq);
        notificationService.sendNotification(pushReq);

        // Client application triggers an emergency security alert broadcast across all channels
        NotificationRequest broadcastAlert = new NotificationRequest(
                "user_id_somani",
                "CRITICAL SECURITY ALERT: Password changed from an unknown IP address.",
                NotificationType.ALL
        );

        // A single clean, uniform execution call!
        notificationService.sendNotification(broadcastAlert);

        // thread-safe and concurrency code below:

        // NotificationChannelRegistry registry = new NotificationChannelRegistry();
        // thread-safe & concurrency: Spin up our concurrent service with a 4-thread worker execution layout
        // ConcurrentNotificationService asyncService = new ConcurrentNotificationService(registry, 4);

        // Simulate 5 client application requests hitting our endpoint simultaneously
        // NotificationRequest request1 = new NotificationRequest("user_1", "Tx Alert: Approved", NotificationType.SMS);
        // NotificationRequest request2 = new NotificationRequest("user_2", "OTP: 1234", NotificationType.SMS);
        // NotificationRequest request3 = new NotificationRequest("user_3", "Welcome aboard!", NotificationType.EMAIL);
        // NotificationRequest request4 = new NotificationRequest("user_4", "Match Live Now!", NotificationType.PUSH);
        // NotificationRequest request5 = new NotificationRequest("user_5", "Password Changed", NotificationType.EMAIL);

        // Dispatches are instant and non-blocking!
        // asyncService.sendNotificationAsync(request1);
        // asyncService.sendNotificationAsync(request2);
        // asyncService.sendNotificationAsync(request3);
        // asyncService.sendNotificationAsync(request4);
        // asyncService.sendNotificationAsync(request5);

        // Give the background worker threads a brief moment to process the queue console streams
        // Thread.sleep(1000);

        // Clean up our compute execution resources
        // asyncService.shutdownExecutorPool();
    }
}
