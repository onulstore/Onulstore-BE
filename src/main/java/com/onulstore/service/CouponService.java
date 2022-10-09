package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.coupon.CouponRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.web.dto.CouponDto;
import com.onulstore.web.dto.CouponDto.RequestCoupon;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void specificUser(CouponDto.RequestCoupon request) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Member toMember = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new Exception(ErrorResult.NOT_EXIST_USER));
        Coupon coupon = request.toCoupon(toMember);
        toMember.getCoupons().add(coupon);
        couponRepository.save(coupon);
    }

    @Transactional
    public void allUser(CouponDto.RequestCoupon request) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        List<Member> memberList = memberRepository.findAll();
        for (Member members : memberList) {
            Coupon coupon = request.toCoupon(members);
            members.getCoupons().add(coupon);
        }
    }

    @Transactional
    public void newUser(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Coupon coupon = Coupon.builder()
            .couponTitle("신규")
            .discountValue(10)
            .leastRequiredValue(0)
            .maxDiscountValue(1000000)
            .discountType(DiscountType.PERCENT)
            .expirationDate(LocalDateTime.now().plusDays(14))
            .member(member)
            .build();

        couponRepository.save(coupon);
        member.getCoupons().add(coupon);
    }

    @Transactional
    public List<RequestCoupon> myCoupons() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        List<RequestCoupon> couponList = couponRepository.findAllByMember(member).stream()
            .map(CouponDto.RequestCoupon::of).collect(Collectors.toList());
        return couponList;
    }
}
