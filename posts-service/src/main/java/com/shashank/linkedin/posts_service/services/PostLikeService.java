package com.shashank.linkedin.posts_service.services;

public interface PostLikeService {
    void likePost(Long postId);

    void unlikePost(Long postId);
}
