package com.onulstore.domain.wishlist;

import com.onulstore.domain.product.Product;
import com.onulstore.web.dto.WishlistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findById(Long id);

    Wishlist findByProductIdAndMemberId(Long productId, Long memberId);

//    Page<WishlistDto.WishlistResponse> findAllByWishlist(Long wishlistId, Pageable pageable);
    List<Wishlist> findAllByMemberId(Long memberId);

}
