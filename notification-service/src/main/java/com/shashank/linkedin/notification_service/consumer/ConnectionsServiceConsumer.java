package com.shashank.linkedin.notification_service.consumer;

import com.shashank.linkedin.connection_service.events.AcceptedConnectionRequestEvent;
import com.shashank.linkedin.connection_service.events.SendConnectionRequestEvent;
import com.shashank.linkedin.notification_service.entity.Notification;
import com.shashank.linkedin.notification_service.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "accepted-connection-request-topic")
    public void handleAcceptedConnectionRequest(AcceptedConnectionRequestEvent acceptedConnectionRequestEvent) {
        Long senderId = acceptedConnectionRequestEvent.getSenderId();
        Long receiverId = acceptedConnectionRequestEvent.getReceiverId();
        String message = String.format("%s has accepted your connection request !", receiverId);
        log.info(message);
        Notification notification = Notification.builder().userId(senderId).message(message).build();
        notificationRepository.save(notification);
    }

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        Long senderId = sendConnectionRequestEvent.getSenderId();
        Long receiverId = sendConnectionRequestEvent.getReceiverId();
        String message = String.format("%s sent your a connection request !", senderId);
        log.info(message);
        Notification notification = Notification.builder().userId(receiverId).message(message).build();
        notificationRepository.save(notification);
    }

}
