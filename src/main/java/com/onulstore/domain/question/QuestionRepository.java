package com.onulstore.domain.question;

import com.onulstore.domain.wishlist.Wishlist;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByProductId(Long productId);
    List<Question> findAllByMemberId(Long memberId);
    Long countByCreatedDateAfter(LocalDateTime localDateTime);
}
