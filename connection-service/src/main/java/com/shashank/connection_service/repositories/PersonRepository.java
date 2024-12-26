package com.shashank.connection_service.repositories;


import com.shashank.connection_service.entities.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    @Query("MATCH (personA:Person) -[:CONNECTED_TO]-> (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH (pA:Person)-[:CONNECTED_TO*2]-(pB:Person) WHERE " +
            "pA.userId = $userId AND NOT (pA)-[:CONNECTED_TO]-(pB) " +
            "AND  pA<>pB RETURN DISTINCT pB")
    List<Person> getSecondDegreeConnections(Long userId);

    @Query("""
             MATCH (pa:Person)-[ct:CONNECTED_TO]-(pb:Person)
             WHERE pa.userId = $firstId AND pb.userId = $secondId
             RETURN count(ct) > 0
            """)
    Boolean checkForConnection(Long firstId, Long secondId);

    @Query("""
            MATCH (pa:Person)-[r:REQUESTED_TO]->(pb:Person)
            WHERE pa.userId = $senderId AND pb.userId = $receiverId
            RETURN count(r) > 0
            """)
    Boolean checkForRequest(Long senderId, Long receiverId);

    @Query("""
            MATCH (p1:Person) , (p2:Person)
            WHERE p1.userId = $senderId AND p2.userId = $receiverId
            CREATE (p1)-[:REQUESTED_TO]->(p2)
            """)
    void addConnectionRequest(Long senderId, Long receiverId);
}
