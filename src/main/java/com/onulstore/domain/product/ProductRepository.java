package com.onulstore.domain.product;

import com.onulstore.domain.enums.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);
    boolean existsByProductName(String email);
    Page<Product> findByBrandId(Long brandId, Pageable pageable);
    Optional<Product> findByCategoryId(Long categoryId);
    List<Product> findAllByProductStatusAndDiscountCheck(ProductStatus productStatus,
        boolean discountCheck);
    List<Product> findAllByCreatedDateAfter(LocalDateTime localDateTime);
    List<Product> findAllByProductStatusAndCreatedDateAfter(ProductStatus productStatus, LocalDateTime localDateTime);
}