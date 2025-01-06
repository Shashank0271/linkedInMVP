package com.shashank.linkedin.posts_service.services.impl;

import com.shashank.linkedin.posts_service.auth.UserContextHolder;
import com.shashank.linkedin.posts_service.dto.PostCreateRequestDto;
import com.shashank.linkedin.posts_service.dto.PostDto;
import com.shashank.linkedin.posts_service.entities.Post;
import com.shashank.linkedin.posts_service.events.PostCreatedEvent;
import com.shashank.linkedin.posts_service.exceptions.ResourceNotFoundException;
import com.shashank.linkedin.posts_service.repositories.PostRepository;
import com.shashank.linkedin.posts_service.services.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostsService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    @Override
    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {

        log.info("creating post ---------------------");
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = Post.builder()
                        .content(postCreateRequestDto.getContent())
                        .userId(userId)
                        .build();

        Post savedPost = postRepository.save(post);

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                                                            .postId(post.getId())
                                                            .creatorId(post.getUserId())
                                                            .content(post.getContent())
                                                            .build();

        kafkaTemplate.send("post-created-topic", postCreatedEvent);

        return modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post requiredPost =
                postRepository.findById(postId)
                              .orElseThrow(() -> new ResourceNotFoundException("post with Id : " + postId + " not found"));
        return modelMapper.map(requiredPost, PostDto.class);
    }

    @Override
    public List<PostDto> getPostsByUserId(Long userId) {
        List<Post> requiredPosts =
                postRepository.findByUserId(userId);
        return requiredPosts.stream()
                            .map((post) -> modelMapper.map(post, PostDto.class))
                            .toList();
    }


}
