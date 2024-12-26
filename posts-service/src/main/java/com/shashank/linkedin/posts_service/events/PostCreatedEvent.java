package com.shashank.linkedin.posts_service.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreatedEvent {
    private Long creatorId;
    private String content;
    private Long postId;
}
