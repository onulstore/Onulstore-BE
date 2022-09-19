package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.question.QuestionRepository;
import com.onulstore.exception.NotExistUserException;
import com.onulstore.web.dto.CartDto;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 질문 등록
    @Transactional
    public void insertQuestion(QuestionDto questionDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        Product product = productRepository.findById(questionDto.getProductId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 상품입니다."));

        Question question = Question.builder()
                            .member(member)
                            .product(product)
                            .title(questionDto.getTitle())
                            .content(questionDto.getContent())
                            .build();

        questionRepository.save(question);
    }

    // 질문 수정
    @Transactional
    public QuestionDto updateQuestion(Long questionId, QuestionDto questionDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        Product product = productRepository.findById(questionDto.getProductId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 상품입니다."));

        Question question = questionRepository.findById(questionId).orElseThrow();
        question.setTitle(questionDto.getTitle());
        question.setContent(questionDto.getContent());

        return QuestionDto.of(questionRepository.save(question));
    }

    // 질문 삭제
    @Transactional
    public void deleteQuestion(Long questionId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        questionRepository.deleteById(questionId);
    }
}
