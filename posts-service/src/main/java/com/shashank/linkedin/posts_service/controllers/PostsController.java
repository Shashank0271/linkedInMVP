package com.shashank.linkedin.posts_service.controllers;

import com.shashank.linkedin.posts_service.auth.UserContextHolder;
import com.shashank.linkedin.posts_service.dto.PostCreateRequestDto;
import com.shashank.linkedin.posts_service.dto.PostDto;
import com.shashank.linkedin.posts_service.services.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class PostsController {
    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto createPostDto) {
        PostDto createdPost =
                postsService.createPost(createPostDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        PostDto requiredPost = postsService.getPostById(postId);
        return new ResponseEntity<>(requiredPost, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPosts(@PathVariable Long userId) {
        List<PostDto> posts =
                postsService.getPostsByUserId(UserContextHolder.getCurrentUserId());
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
