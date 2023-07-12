package com.sparta.lv3.repository;

import com.sparta.lv3.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByPostIdAndIdOrderByCreatedAtDesc(Long postId, Long id);

}
