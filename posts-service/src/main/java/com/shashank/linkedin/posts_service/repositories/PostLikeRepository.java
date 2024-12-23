package com.shashank.linkedin.posts_service.repositories;

import com.shashank.linkedin.posts_service.entities.PostLike;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike
        , Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    @Transactional
    void deleteByPostIdAndUserId(Long postId, Long userId);
}
