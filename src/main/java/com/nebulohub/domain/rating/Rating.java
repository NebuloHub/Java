package com.nebulohub.domain.rating;

import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "ratings",

    uniqueConstraints = @UniqueConstraint(
            name = "uq_user_post_rating",
            columnNames = {"id_user", "id_post"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ratings_seq")
    @SequenceGenerator(name = "ratings_seq", sequenceName = "ratings_seq", allocationSize = 1)
    @Column(name = "id_rating")
    private Long id;

    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "rating_value", nullable = false)
    private Integer ratingValue;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_post", nullable = false)
    private Post post;
}