package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.PasswordDto;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.ProductDto.ProductResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDto.MemberResponse getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .map(MemberDto.MemberResponse::of)
            .orElseThrow(() -> new Exception(ErrorResult.NOT_EXIST_USER));
    }

    @Transactional
    public MemberDto.MemberResponse updateProfile(MemberDto.UpdateRequest updateRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Member updateMember = member.updateProfile(updateRequest);
        return MemberDto.MemberResponse.of(memberRepository.save(updateMember));
    }

    @Transactional
    public void deleteProfile() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        memberRepository.delete(member);
    }

    @Transactional
    public void updatePassword(PasswordDto passwordDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        if (!passwordDto.getNewPassword().equals(passwordDto.getNewPasswordConfirm())) {
            throw new Exception(ErrorResult.UPDATE_PASSWORD);
        }

        Member updateMember = member.updatePassword(
            passwordEncoder.encode(passwordDto.getNewPassword()));
        memberRepository.save(updateMember);
    }

    @Transactional
    public ArrayList<ProductResponse> latestProduct(HttpServletRequest request) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        HttpSession session = request.getSession();

        ArrayList<Product> latest = (ArrayList) session.getAttribute("List");
        ArrayList<ProductDto.ProductResponse> recentlyViewed = new ArrayList<>();
        for (Product product : latest) {
            recentlyViewed.add(ProductDto.ProductResponse.of(product));
        }
        return recentlyViewed;
    }

    @Transactional
    public List<Integer> memberDashBoard(LocalDateTime localDateTime){
        List<Member> memberList = memberRepository.findAllByAuthorityAndCreatedDateAfter(Authority.ROLE_USER, localDateTime);
        List<Member> sellerList = memberRepository.findAllByAuthorityAndCreatedDateAfter(Authority.ROLE_SELLER, localDateTime);
        List<Integer> memberAmount = new ArrayList<>();
        memberAmount.add(memberList.size());
        memberAmount.add(sellerList.size());
        return memberAmount;
    }
}
