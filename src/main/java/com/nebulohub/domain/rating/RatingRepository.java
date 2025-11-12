package com.nebulohub.domain.rating;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    /**
     * Checks if a rating already exists for this combination of user and post.
     */
    Optional<Rating> findByUserIdAndPostId(Long userId, Long postId);

    /**
     * Finds all ratings for a specific post, fetching the user data eagerly.
     */
    @Query("SELECT r FROM Rating r JOIN FETCH r.user WHERE r.post.id = :postId")
    Page<Rating> findAllByPostIdWithUser(Long postId, Pageable pageable);

    /**
     * Finds all ratings by a specific user, fetching the post data eagerly.
     */
    @Query("SELECT r FROM Rating r JOIN FETCH r.post WHERE r.user.id = :userId")
    Page<Rating> findAllByUserIdWithPost(Long userId, Pageable pageable);

    /**
     * Finds all ratings for a post. Used for average calculation.
     */
    List<Rating> findAllByPostId(Long postId);
}