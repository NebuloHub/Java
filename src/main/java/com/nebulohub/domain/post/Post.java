package com.nebulohub.domain.post;

import com.nebulohub.domain.comment.Comment;
import com.nebulohub.domain.rating.Rating;
import com.nebulohub.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_seq")
    @SequenceGenerator(name = "posts_seq", sequenceName = "posts_seq", allocationSize = 1)
    @Column(name = "id_post")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    /**
     * **NOVO CAMPO**
     * Armazena a URL para a imagem do post (opcional).
     */
    @Column(name = "image_url", length = 2000)
    private String imageUrl;

    @NotNull
    @Column(name = "avg_rating", nullable = false)
    private Double avgRating = 0.0;

    @NotNull
    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount = 0;

    @NotNull
    @Column(name = "comment_count", nullable = false)
    private Integer commentCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // --- Relationships ---

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;
}