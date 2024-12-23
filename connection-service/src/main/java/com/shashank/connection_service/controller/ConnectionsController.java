package com.shashank.connection_service.controller;

import com.shashank.connection_service.entities.Person;
import com.shashank.connection_service.services.ConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections() {
        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections());
    }

    @GetMapping("/{userId}/second-degree")
    public ResponseEntity<List<Person>> getSecondDegreeConnections(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.getSecondDegreeConnections(userId));
    }

}
