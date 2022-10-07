package com.onulstore.domain.coupon;

import com.onulstore.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findAllByMember(Member member);
}
