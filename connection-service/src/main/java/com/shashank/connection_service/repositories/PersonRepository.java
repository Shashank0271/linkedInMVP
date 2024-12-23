package com.shashank.connection_service.repositories;


import com.shashank.connection_service.entities.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    @Query("MATCH (personA:Person) -[:CONNECTED_TO]- (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH (pA:Person)-[:CONNECTED_TO*2]-(pB:Person) WHERE " +
            "pA.userId = $userId AND NOT (pA)-[:CONNECTED_TO]-(pB) " +
            "AND NOT pA=pB RETURN DISTINCT pB")
    List<Person> getSecondDegreeConnections(Long userId);

}
