package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.coupon.CouponRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.CouponStatus;
import com.onulstore.domain.enums.CustomErrorResult;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.web.dto.CouponDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public CouponDto.ResponseCoupon specificUser(CouponDto.RequestCoupon request) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        Member toMember = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Coupon coupon = request.toCoupon(toMember);
        toMember.getCoupons().add(coupon);
        couponRepository.save(coupon);
        return CouponDto.ResponseCoupon.of(coupon);
    }

    @Transactional
    public void allUser(CouponDto.RequestCoupon request) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
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
            () -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Coupon coupon = Coupon.builder()
            .couponTitle("신규")
            .couponStatus(CouponStatus.DEFAULT)
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
    public List<CouponDto.ResponseCoupon> myCoupons() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));

        List<CouponDto.ResponseCoupon> couponList = couponRepository.findAllByMember(member).stream()
            .map(CouponDto.ResponseCoupon::of).collect(Collectors.toList());
        return couponList;
    }
}
