package org.mario.dev.service;

import lombok.extern.slf4j.Slf4j;
import org.mario.dev.dto.ReviewDto;
import org.mario.dev.entity.Review;
import org.mario.dev.repository.ProductRepository;
import org.mario.dev.repository.ReviewRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.mario.dev.dto.ReviewDto.mapToDto;

@Slf4j
@ApplicationScoped
@Transactional
public class ReviewService {

    @Inject
    ReviewRepository reviewRepository;
    @Inject
    ProductRepository productRepository;

    public List<ReviewDto> findReviewsByProductId(Long id) {
        log.debug("Request to get all Reviews");
        return this.reviewRepository.findReviewsByProductId(id)
                .stream()
                .map(ReviewDto::mapToDto)
                .collect(Collectors.toList());
    }

    public ReviewDto findById(Long id) {
        log.debug("Request to get Review : {}", id);
        return this.reviewRepository.findById(id)
                .map(ReviewDto::mapToDto)
                .orElse(null);
    }

    public ReviewDto create(ReviewDto reviewDto, Long productId) {
        log.debug("Request to create Review : {} ofr the Product {}", reviewDto, productId);

        var product = this.productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product with ID:" + productId + " was not found !"));

        var savedReview = this.reviewRepository.saveAndFlush(
                new Review(
                        reviewDto.getTitle(),
                        reviewDto.getDescription(),
                        reviewDto.getRating()
                )
        );

        product.getReviews().add(savedReview);
        this.productRepository.saveAndFlush(product);

        return mapToDto(savedReview);
    }

    public void delete(Long reviewId) {
        log.debug("Request to delete Review : {}", reviewId);

        var review = this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Product with ID:" + reviewId + " was not found !"));

        var product = this.productRepository.findProductByReviewId(reviewId);

        product.getReviews().remove(review);

        this.productRepository.saveAndFlush(product);
        this.reviewRepository.delete(review);
    }
}