package com.shashank.linkedin.notification_service.consumer;

import com.shashank.linkedin.notification_service.clients.ConnectionsClient;
import com.shashank.linkedin.notification_service.dto.PersonDto;
import com.shashank.linkedin.notification_service.entity.Notification;
import com.shashank.linkedin.notification_service.repositories.NotificationRepository;
import com.shashank.linkedin.posts_service.events.PostCreatedEvent;
import com.shashank.linkedin.posts_service.events.PostLikeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsServiceConsumer {
    private final NotificationRepository notificationRepository;
    private final ConnectionsClient connectionsClient;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("--------------consumed post-created event------------");
        List<PersonDto> firstConnections = connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());
        final String message = String.format("Your connection %s shared a post . Check it out !",
                postCreatedEvent.getCreatorId());
        firstConnections.forEach(connection -> {
            sendNotification(connection.getUserId(), message);
        });
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikeEvent postLikeEvent) {
        log.info("--------------consumed post-liked event------------");
        final String message = String.format("%s liked your post !", postLikeEvent.getLikedByUserId());
        sendNotification(postLikeEvent.getCreatorId(), message);
    }

    private void sendNotification(Long userId, String message) {
        notificationRepository.save(Notification.builder().userId(userId).message(message).build());
    }

}
