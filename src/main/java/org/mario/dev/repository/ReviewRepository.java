package org.mario.dev.repository;

import org.mario.dev.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select p.reviews from Product p where p.id = ?1")
    List<Review> findReviewsByProductId(Long id);
}
