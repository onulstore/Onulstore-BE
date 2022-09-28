package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.question.QuestionRepository;
import com.onulstore.exception.UserException;
import com.onulstore.web.dto.QuestionDto;
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
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(questionDto.getProductId()).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

        Question question = Question.builder()
                            .member(member)
                            .product(product)
                            .title(questionDto.getTitle())
                            .content(questionDto.getContent())
                .answerState(questionDto.getAnswerStatus())
                            .build();
        questionRepository.save(question);
        question.unAnswered();
    }

    // 질문 수정
    @Transactional
    public QuestionDto updateQuestion(Long questionId, QuestionDto questionDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(questionDto.getProductId()).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new UserException(UserErrorResult.NOT_EXIST_QUESTION));

        if (!member.getId().equals(question.getMember().getId())) {
            throw new UserException(UserErrorResult.USER_NOT_MATCH);
        }
        question.setTitle(questionDto.getTitle());
        question.setContent(questionDto.getContent());
        return QuestionDto.of(questionRepository.save(question));
    }

    // 질문 삭제
    @Transactional
    public void deleteQuestion(Long questionId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));

        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_QUESTION));

        if (!member.getId().equals(question.getMember().getId())){
            throw new UserException(UserErrorResult.USER_NOT_MATCH);
        }
        questionRepository.deleteById(questionId);
    }

    // 질문 상세 조회
    @Transactional
    public QuestionDto getQuestion(Long productId, Long questionId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new UserException(UserErrorResult.NOT_EXIST_QUESTION));

        return QuestionDto.of(question);
    }

    // 질문 전체 조회
    @Transactional
    public List<QuestionDto> getQuestionList(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

        List<Question> questions = questionRepository.findAllByProductId(product.getId());
        List<QuestionDto> questionList = new ArrayList<>();

        for (Question question : questions) {
            questionList.add(QuestionDto.of(question));
        }
        return questionList;
    }

}
