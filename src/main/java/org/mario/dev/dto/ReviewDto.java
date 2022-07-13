package org.mario.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mario.dev.entity.Review;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String title;
    private String description;
    private Long rating;

    public static ReviewDto mapToDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getTitle(),
                review.getDescription(),
                review.getRating()
        );
    }
}
