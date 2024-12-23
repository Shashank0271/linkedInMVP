package com.shashank.linkedin.posts_service.services;

import com.shashank.linkedin.posts_service.dto.PostCreateRequestDto;
import com.shashank.linkedin.posts_service.dto.PostDto;

import java.util.List;

public interface PostsService {
    PostDto createPost(PostCreateRequestDto postCreateRequestDto);

    PostDto getPostById(Long postId);

    List<PostDto> getPostsByUserId(Long userId);
}
