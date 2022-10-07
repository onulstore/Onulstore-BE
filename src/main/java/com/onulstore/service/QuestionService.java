package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.question.QuestionRepository;
import com.onulstore.web.dto.QuestionDto;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
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

    // 질문 등록
    @Transactional
    public void insertQuestion(QuestionDto questionDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(questionDto.getProductId()).orElseThrow(
            () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));

        Question question = Question.builder()
            .member(member)
            .product(product)
            .title(questionDto.getTitle())
            .content(questionDto.getContent())
            .secret(questionDto.getSecret())
            .answerStatus(questionDto.getAnswerStatus())
            .build();

        question.unAnswered();
        questionRepository.save(question);
    }

    // 질문 수정
    @Transactional
    public QuestionDto updateQuestion(Long questionId, QuestionDto questionDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(questionDto.getProductId()).orElseThrow(
            () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));

        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_QUESTION));

        if (!member.getId().equals(question.getMember().getId())) {
            throw new Exception(ErrorResult.USER_NOT_MATCH);
        }
        question.setTitle(questionDto.getTitle());
        question.setContent(questionDto.getContent());

        return QuestionDto.of(questionRepository.save(question));
    }

    // 질문 삭제
    @Transactional
    public void deleteQuestion(Long questionId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_QUESTION));

        if (!member.getId().equals(question.getMember().getId())) {
            throw new Exception(ErrorResult.USER_NOT_MATCH);
        }
        questionRepository.deleteById(questionId);
    }

    // 질문 상세 조회
    @Transactional
    public QuestionDto getQuestion(Long productId, Long questionId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));
        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_QUESTION));

        if (question.getSecret() == 'Y') {
            if (!member.getId().equals(question.getMember().getId()) || !member.getAuthority()
                .equals(Authority.ROLE_ADMIN.getKey())) {
                throw new Exception(ErrorResult.SECRET_QUESTION);
            }
            return QuestionDto.of(question);
        }
        return QuestionDto.of(question);
    }

    // 질문 전체 조회(상품별)
    @Transactional
    public List<QuestionDto> getQuestionList(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));

        List<Question> questions = questionRepository.findAllByProductId(product.getId());
        List<QuestionDto> questionList = new ArrayList<>();

        for (Question question : questions) {
            questionList.add(QuestionDto.of(question));
        }
        return questionList;
    }

    // 질문 전체 조회(멤버별)
    @Transactional
    public List<QuestionDto> getMemberQuestionList() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        List<Question> questions = questionRepository.findAllByMemberId(member.getId());
        List<QuestionDto> questionList = new ArrayList<>();

        for (Question question : questions) {
            questionList.add(QuestionDto.of(question));
        }
        return questionList;
    }

    @Transactional
    public Integer questionDashBoard(LocalDateTime localDateTime) {
        List<Question> questionList = questionRepository.findAllByCreatedDateAfter(localDateTime);
        return questionList.size();
    }

}
