package lowLevelDesign.notificationSystem.service;

import lowLevelDesign.notificationSystem.domain.NotificationChannel;
import lowLevelDesign.notificationSystem.domain.NotificationRequest;
import lowLevelDesign.notificationSystem.strategy.NotificationChannelRegistry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentNotificationService {
    private final NotificationChannelRegistry registry;

    // 🌟 CONCURRENCY WIN: Thread Pool manages a dedicated set of worker threads.
    // It encapsulates an internal thread-safe LinkedBlockingQueue to absorb transactional bursts.
    private final ExecutorService workerThreadPool;

    public ConcurrentNotificationService(NotificationChannelRegistry registry, int dynamicPoolSize) {
        this.registry = registry;
        // Instantiate a fixed worker thread pool to handle IO-heavy network calls to SMS/Email gateways
        this.workerThreadPool = Executors.newFixedThreadPool(dynamicPoolSize);
    }

    /**
     * Non-Blocking Ingress: Instantly drops the task into the queue and frees the calling thread.
     */
    public void sendNotificationAsync(NotificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request payload cannot be null.");
        }

        // 🌟 PRODUCER STEP: Submit an asynchronous worker task to the thread pool queue
        workerThreadPool.submit(() -> {
            try {
                // 🌟 CONSUMER STEP: Executed independently by a background worker thread
                NotificationChannel targetedChannel = registry.getSupportedChannel(request.getNotificationType());
                targetedChannel.send(request);
            } catch (Exception errorException) {
                // Defensive Error Isolation: Prevent an external gateway crash from killing our worker thread
                System.err.println("Failed to dispatch notification task sequentially: " + errorException.getMessage());
            }
        });
    }

    /**
     * Graceful Decommission Hook: Closes the stream pipelines safely on system shutdown.
     */
    public void shutdownExecutorPool() {
        System.out.println("Initiating graceful teardown of notification worker threads...");
        workerThreadPool.shutdown(); // Reject new incoming requests, but allow queued items to finish
        try {
            if (!workerThreadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                workerThreadPool.shutdownNow(); // Force kill threads if they take more than 5 seconds
            }
            System.out.println("Notification pool completely terminated.");
        } catch (InterruptedException e) {
            workerThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
