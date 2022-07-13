package org.mario.dev.service;

import lombok.extern.slf4j.Slf4j;
import org.mario.dev.dto.ProductDto;
import org.mario.dev.entity.Product;
import org.mario.dev.entity.enums.ProductStatus;
import org.mario.dev.repository.CategoryRepository;
import org.mario.dev.repository.ProductRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mario.dev.dto.ProductDto.mapToDto;

@Slf4j
@ApplicationScoped
@Transactional
public class ProductService {

    @Inject
    ProductRepository productRepository;
    @Inject
    CategoryRepository categoryRepository;


    public List<ProductDto> findAll() {
        log.debug("Request to get all Products");
        return this.productRepository.findAll()
                .stream()
                .map(ProductDto::mapToDto)
                .collect(Collectors.toList());
    }

    public ProductDto findById(Long id) {
        log.debug("Request to get Product : {}", id);
        return this.productRepository.findById(id)
                .map(ProductDto::mapToDto)
                .orElse(null);
    }

    public ProductDto create(ProductDto productDto) {
        log.debug("Request to create Product : {}", productDto);

        return mapToDto(this.productRepository.save(
                new Product(
                        productDto.getName(),
                        productDto.getDescription(),
                        productDto.getPrice(),
                        ProductStatus.valueOf(productDto.getStatus()),
                        productDto.getSalesCounter(),
                        Collections.emptySet(),
                        categoryRepository.findById(productDto.getCategoryId()).orElse(null)
                )));
    }

    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        this.productRepository.deleteById(id);
    }

    public List<ProductDto> findByCategoryId(Long id) {
        return this.productRepository.findByCategoryId(id).stream()
                .map(ProductDto::mapToDto)
                .collect(Collectors.toList());
    }

    public Long countAll() {
        return this.productRepository.count();
    }

    public Long countByCategoryId(Long id) {
        return this.productRepository.countAllByCategoryId(id);
    }
}