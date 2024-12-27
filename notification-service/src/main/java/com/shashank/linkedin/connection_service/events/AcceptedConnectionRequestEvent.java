package com.shashank.linkedin.connection_service.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptedConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}
