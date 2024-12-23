package com.shashank.linkedin.posts_service.services;

public interface PostLikeService {
    void likePost(Long postId, Long userId);

    void unlikePost(Long postId, Long userId);
}
