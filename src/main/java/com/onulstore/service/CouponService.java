package com.onulstore.service;

import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.coupon.CouponRepository;
import com.onulstore.domain.enums.CouponStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.config.exception.Exception;
import com.onulstore.web.dto.CouponDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void specificUser(CouponDto couponDto) {
        if(couponDto.getMemberId().equals("ALL")) {
            List<Member> memberList = memberRepository.findAll();
            for(Member member : memberList) {
                Coupon coupon = couponDto.toCoupon(member);
                member.getCoupons().add(coupon);
                couponRepository.save(coupon);
            }
        } else {
            Member member = memberRepository.findById((Long.parseLong(couponDto.getMemberId())))
                .orElseThrow(() -> new Exception(ErrorResult.NOT_EXIST_USER));
            Coupon coupon = couponDto.toCoupon(member);
            member.getCoupons().add(coupon);
            couponRepository.save(coupon);
        }
    }

    @Transactional
    public void newUser(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail).get();
        Coupon coupon = Coupon.builder()
            .member(member)
            .couponTitle("신규")
            .expirationDate(LocalDateTime.now().plusDays(14))
            .discountValue(10)
            .discountType(DiscountType.PERCENT)
            .leastRequiredValue(0)
            .maxDiscountValue(1000000)
            .couponStatus(CouponStatus.DEFAULT)
            .categoryCode("ALL")
            .build();

        couponRepository.save(coupon);
        member.getCoupons().add(coupon);
    }
}
