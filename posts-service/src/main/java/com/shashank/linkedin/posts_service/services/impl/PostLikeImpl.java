package com.shashank.linkedin.posts_service.services.impl;

import com.shashank.linkedin.posts_service.auth.UserContextHolder;
import com.shashank.linkedin.posts_service.entities.Post;
import com.shashank.linkedin.posts_service.entities.PostLike;
import com.shashank.linkedin.posts_service.events.PostLikeEvent;
import com.shashank.linkedin.posts_service.exceptions.BadRequestException;
import com.shashank.linkedin.posts_service.exceptions.ResourceNotFoundException;
import com.shashank.linkedin.posts_service.repositories.PostLikeRepository;
import com.shashank.linkedin.posts_service.repositories.PostRepository;
import com.shashank.linkedin.posts_service.services.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final KafkaTemplate<Long, PostLikeEvent> kafkaTemplate;

    @Override
    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post with id " + postId
                        + " does not exist"));

        boolean alreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);
        if (alreadyLiked) {
            throw new BadRequestException("Cannot like the same " +
                    "post again!");
        }

        PostLike postLike = PostLike.
                builder().
                postId(postId).
                userId(userId).
                build();
        postLikeRepository.save(postLike);

        PostLikeEvent postLikeEvent = PostLikeEvent
                .builder()
                .likedByUserId(userId)
                .creatorId(post.getUserId())
                .postId(post.getId())
                .build();

        kafkaTemplate.send("post-liked-topic", postId, postLikeEvent); // the notification for all posts having same id will be in the same order

    }

    @Override
    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        boolean postExists = postRepository.existsById(postId);
        if (!postExists) {
            throw new ResourceNotFoundException("post with id : " + postId + " does not exist");
        }
        boolean likedPost =
                postLikeRepository.existsByPostIdAndUserId(postId, userId);
        if (!likedPost) {
            throw new BadRequestException("cannot unlike a post " +
                    "which has not been liked");
        }
        postLikeRepository.deleteByPostIdAndUserId(postId, userId);
    }
}
