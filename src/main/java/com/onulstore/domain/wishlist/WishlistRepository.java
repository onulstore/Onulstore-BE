package com.onulstore.domain.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findById(Long id);

    Wishlist findByProductIdAndMemberId(Long productId, Long memberId);

    List<Wishlist> findAllByMemberId(Long memberId);

}
