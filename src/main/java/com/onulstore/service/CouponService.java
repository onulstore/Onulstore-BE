package com.onulstore.service;

import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.coupon.CouponRepository;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.exception.UserException;
import com.onulstore.web.dto.CouponDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void specificUser(CouponDto.RequestCoupon request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_EXIST_USER));
        Coupon coupon = request.toCoupon(member);
        member.getCoupons().add(coupon);
        couponRepository.save(coupon);
    }


    public void allUser(CouponDto.RequestCoupon request) {
        List<Member> memberList = memberRepository.findAll();

        for (Member member : memberList) {
            Coupon coupon = request.toCoupon(member);
            member.getCoupons().add(coupon);
        }
    }

    @Transactional
    public void newUser(String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).get();
        Coupon coupon = Coupon.builder()
                .couponTitle("신규")
                .discountValue(10)
                .leastRequiredValue(0)
                .maxDiscountValue(1000000)
                .discountType(DiscountType.PERCENT)
                .member(member)
                .build();

        coupon.settingExpirationDate();
        couponRepository.save(coupon);
        member.getCoupons().add(coupon);
    }
}
