package org.mario.dev.service;

import lombok.extern.slf4j.Slf4j;
import org.mario.dev.dto.CategoryDto;
import org.mario.dev.dto.ProductDto;
import org.mario.dev.entity.Category;
import org.mario.dev.repository.CategoryRepository;
import org.mario.dev.repository.ProductRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.mario.dev.dto.CategoryDto.mapToDto;

@Slf4j
@ApplicationScoped
@Transactional
public class CategoryService {
    @Inject
    CategoryRepository categoryRepository;

    @Inject
    ProductRepository productRepository;

    public List<CategoryDto> findAll() {
        log.debug("Request to get all Categories");
        return this.categoryRepository.findAll()
                .stream()
                .map(category -> mapToDto(category, productRepository.countAllByCategoryId(category.getId())))
                .collect(Collectors.toList());
    }

    public CategoryDto findById(Long id) {
        log.debug("Request to get Category : {}", id);
        return this.categoryRepository.findById(id)
                .map(category -> mapToDto(category, productRepository.countAllByCategoryId(category.getId())))
                .orElse(null);
    }

    public CategoryDto create(CategoryDto categoryDto) {
        log.debug("Request to create Category : {}", categoryDto);
        return mapToDto(this.categoryRepository
                .save(new Category(categoryDto.getName(), categoryDto.getDescription())), 0L);
    }

    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        log.debug("Deleting all products for the Category : {}", id);
        this.productRepository.deleteAllByCategoryId(id);
        log.debug("Deleting Category : {}", id);
        this.categoryRepository.deleteById(id);
    }

    public List<ProductDto> findProductsByCategoryId(Long id) {
        return this.productRepository.findAllByCategoryId(id)
                .stream()
                .map(ProductDto::mapToDto)
                .collect(Collectors.toList());
    }
}
