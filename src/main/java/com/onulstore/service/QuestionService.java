package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.CustomErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.question.QuestionRepository;
import com.onulstore.domain.questionAnswer.QuestionAnswer;
import com.onulstore.domain.questionAnswer.QuestionAnswerRepository;
import com.onulstore.web.dto.QuestionDto;
import java.time.LocalDateTime;
import com.onulstore.web.dto.QuestionDto.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final QuestionAnswerRepository questionAnswerRepository;

    // 질문 등록
    @Transactional
    public void insertQuestion(QuestionDto.QuestionRequest questionDto) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(questionDto.getProductId()).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        Question question = Question.builder()
            .member(member)
            .product(product)
            .title(questionDto.getTitle())
            .content(questionDto.getContent())
            .secret(questionDto.isSecret())
            .build();

        questionRepository.save(question);
    }

    // 질문 수정
    @Transactional
    public QuestionResponse updateQuestion(Long questionId, QuestionDto.QuestionRequest questionDto) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(questionDto.getProductId()).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));
        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new CustomException(CustomErrorResult.NOT_EXIST_QUESTION));
        if (!member.getId().equals(question.getMember().getId())) {
            throw new CustomException(CustomErrorResult.USER_NOT_MATCH);
        }
        question.setTitle(questionDto.getTitle());
        question.setContent(questionDto.getContent());

        return QuestionDto.QuestionResponse.of(questionRepository.save(question));
    }

    // 질문 삭제
    @Transactional
    public void deleteQuestion(Long questionId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new CustomException(CustomErrorResult.NOT_EXIST_QUESTION));

        if (!member.getId().equals(question.getMember().getId())) {
            throw new CustomException(CustomErrorResult.USER_NOT_MATCH);
        }
        questionRepository.deleteById(questionId);
    }

    // 질문 상세 조회
    @Transactional
    public QuestionDto.QuestionRes getQuestion(Long productId, Long questionId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));
        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new CustomException(CustomErrorResult.NOT_EXIST_QUESTION));
        QuestionAnswer questionAnswer = questionAnswerRepository.findByQuestionId(question.getId()).orElse(null);

        if (question.isSecret() == true) {
            if (!(member.getId().equals(question.getMember().getId()) || member.getAuthority()
                .equals(Authority.ROLE_ADMIN.getKey()))) {
                throw new CustomException(CustomErrorResult.SECRET_QUESTION);
            }
            return QuestionDto.QuestionRes.of(question, questionAnswer);
        }
        return QuestionDto.QuestionRes.of(question, questionAnswer);
    }

    // 질문 전체 조회(상품별)
    @Transactional
    public List<QuestionDto.QuestionResponse> getQuestionList(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        List<Question> questions = questionRepository.findAllByProductId(product.getId());
        List<QuestionDto.QuestionResponse> questionList = new ArrayList<>();

        for (Question question : questions) {
            questionList.add(QuestionDto.QuestionResponse.of(question));
        }
        return questionList;
    }

    // 질문 전체 조회(멤버별)
    @Transactional
    public List<QuestionDto.QuestionResponse> getMemberQuestionList() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));

        List<Question> questions = questionRepository.findAllByMemberId(member.getId());
        List<QuestionDto.QuestionResponse> questionList = new ArrayList<>();

        for (Question question : questions) {
            questionList.add(QuestionDto.QuestionResponse.of(question));
        }
        return questionList;
    }

    @Transactional
    public Long questionDashBoard(LocalDateTime localDateTime) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        Long questions = questionRepository.countByCreatedDateAfter(localDateTime);
        return questions;
    }

}
