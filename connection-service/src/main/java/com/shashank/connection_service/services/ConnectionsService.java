package com.shashank.connection_service.services;

import com.shashank.connection_service.auth.UserContextHolder;
import com.shashank.connection_service.entities.Person;
import com.shashank.connection_service.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConnectionsService {

    private final PersonRepository personRepository;

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

        if (Objects.equals(userId, receiverId))
            throw new RuntimeException("sender and receiver cannot be same !");

        Boolean requestExists = personRepository.checkForRequest(userId, receiverId);
        if (requestExists)
            throw new RuntimeException("connection request already exists !");

        Boolean connectionExists = personRepository.checkForConnection(userId, receiverId);
        if (connectionExists)
            throw new RuntimeException("connection already exists !");

        personRepository.addConnectionRequest(userId, receiverId);
        return true;
    }

}

