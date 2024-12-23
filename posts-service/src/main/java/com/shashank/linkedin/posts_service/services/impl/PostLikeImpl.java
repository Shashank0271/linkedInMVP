package com.shashank.linkedin.posts_service.services.impl;

import com.shashank.linkedin.posts_service.entities.PostLike;
import com.shashank.linkedin.posts_service.exceptions.BadRequestException;
import com.shashank.linkedin.posts_service.exceptions.ResourceNotFoundException;
import com.shashank.linkedin.posts_service.repositories.PostLikeRepository;
import com.shashank.linkedin.posts_service.repositories.PostRepository;
import com.shashank.linkedin.posts_service.services.PostLikeService;
import com.shashank.linkedin.posts_service.services.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final PostsService postsService;

    @Override
    public void likePost(Long postId, Long userId) {

        boolean postExists = postRepository.existsById(postId);
        if (!postExists) {
            throw new ResourceNotFoundException("post with id :" + postId + " does not exist");
        }
        boolean alreadyLiked =
                postLikeRepository.existsByPostIdAndUserId(postId,
                        userId);
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
    }

    @Override
    public void unlikePost(Long postId, Long userId) {
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
