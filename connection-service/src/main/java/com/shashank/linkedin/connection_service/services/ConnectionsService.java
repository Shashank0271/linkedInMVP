package com.shashank.linkedin.connection_service.services;

import com.shashank.linkedin.connection_service.auth.UserContextHolder;
import com.shashank.linkedin.connection_service.entities.Person;
import com.shashank.linkedin.connection_service.events.AcceptedConnectionRequestEvent;
import com.shashank.linkedin.connection_service.events.SendConnectionRequestEvent;
import com.shashank.linkedin.connection_service.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, AcceptedConnectionRequestEvent> acceptedConnectionRequestEventKafkaTemplate;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate;

    public List<Person> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("getting first degree connection for user {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    public List<Person> getSecondDegreeConnections(Long userId) {
        return personRepository.getSecondDegreeConnections(userId);
    }

    public Boolean sendConnectionRequest(Long receiverId) {
        Long userId = UserContextHolder.getCurrentUserId();

        if (Objects.equals(userId, receiverId)) {
            throw new RuntimeException("sender and receiver cannot be same !");
        }

        Boolean requestExists = personRepository.checkForRequest(userId, receiverId);
        if (requestExists) {
            throw new RuntimeException("connection request already exists !");
        }

        Boolean connectionExists = personRepository.checkForConnection(userId, receiverId);
        if (connectionExists) {
            throw new RuntimeException("connection already exists !");
        }

        personRepository.addConnectionRequest(userId, receiverId);
        SendConnectionRequestEvent sendConnectionRequestEvent =
                SendConnectionRequestEvent.builder().senderId(userId).receiverId(receiverId).build();

        sendConnectionRequestEventKafkaTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);
        return true;
    }

    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        if (personRepository.checkForConnection(senderId, receiverId)) {
            throw new RuntimeException("users are already connected !");
        }
        log.info("User id : {} accepted connection request from sender Id : {}", receiverId, senderId);

        personRepository.addConnection(senderId, receiverId);
        AcceptedConnectionRequestEvent acceptedConnectionRequestEvent
                = AcceptedConnectionRequestEvent.builder().senderId(senderId).receiverId(receiverId).build();

        acceptedConnectionRequestEventKafkaTemplate.send("accepted-connection-request-topic",
                acceptedConnectionRequestEvent);
        return true;
    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        Boolean requestExists = personRepository.checkForRequest(senderId, receiverId);
        if (!requestExists)
            throw new RuntimeException("request by user id " + senderId + " does not exist");

        log.info("User id : {} has rejected con req by user id {}", receiverId, senderId);
        personRepository.rejectConnectionRequest(senderId, receiverId);
        return true;
    }

}

