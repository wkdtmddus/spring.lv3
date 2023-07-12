package com.sparta.lv2.repository;

import com.sparta.lv2.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByPostIdAndIdOrderByCreatedAtDesc(Long postId, Long id);

}
