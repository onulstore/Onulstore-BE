package com.onulstore.domain.wishlist;

import com.onulstore.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findById(Long id);

    Wishlist findByProductIdAndMemberId(Long productId, Long memberId);

}
