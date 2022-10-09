package com.onulstore.domain.review;

import com.onulstore.domain.member.Member;
import com.onulstore.domain.product.Product;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByMemberId(Long memberId);

    List<Review> findAllByProductId(Long productId, Pageable pageable);

    List<Review> findAllByCreatedDateAfter(LocalDateTime localDateTime);

    Long countByProduct(Product product);

    Optional<Review> findByMemberAndId(Member member, Long reviewId);
}
