package com.shashank.connection_service.services;

import com.shashank.connection_service.auth.UserContextHolder;
import com.shashank.connection_service.entities.Person;
import com.shashank.connection_service.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConnectionsService {

    private final PersonRepository personRepository;

    public List<Person> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        return personRepository.getFirstDegreeConnections(userId);
    }

    public List<Person> getSecondDegreeConnections(Long userId) {
        return personRepository.getSecondDegreeConnections(userId);
    }

}

