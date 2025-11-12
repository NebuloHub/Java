package com.nebulohub.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Finds all posts, joining the User entity to avoid N+1 query problems.
     * The results are ordered by creation date, newest first.
     */
    @Query("SELECT p FROM Post p JOIN FETCH p.user u ORDER BY p.createdAt DESC")
    Page<Post> findAllWithUserOrderByCreatedAtDesc(Pageable pageable);
}