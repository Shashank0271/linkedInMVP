package com.shashank.linkedin.connection_service.events;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AcceptedConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}
