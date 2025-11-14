package com.nebulohub.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Finds all comments for a specific post, fetching the user data eagerly.
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.user u WHERE c.post.id = :postId ORDER BY c.createdAt DESC")
    Page<Comment> findAllByPostIdWithUser(Long postId, Pageable pageable);

    /**
     * Finds all comments by a specific user, fetching the post data eagerly.
     * Ordered by creation date (newest first) to show user activity.
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.post p WHERE c.user.id = :userId ORDER BY c.createdAt DESC")
    Page<Comment> findAllByUserIdWithPost(Long userId, Pageable pageable);


    /**
     * Finds the top 3 most recent comments for a single post.
     */
    List<Comment> findTop3ByPostIdOrderByCreatedAtDesc(Long postId);

    /**
     * Gets the total number of comments for a post.
     */
    long countByPostId(Long postId);
}