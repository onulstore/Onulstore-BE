package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.CustomErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.question.QuestionRepository;
import com.onulstore.domain.questionAnswer.QuestionAnswer;
import com.onulstore.domain.questionAnswer.QuestionAnswerRepository;
import com.onulstore.web.dto.QuestionAnswerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final QuestionAnswerRepository questionAnswerRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    // 답변 등록
    @Transactional
    public void insertAnswer(Long questionId, QuestionAnswerDto questionAnswerDto) {
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


        QuestionAnswer answer = QuestionAnswer.builder()
            .member(member)
            .question(question)
            .answer(questionAnswerDto.getAnswer())
            .build();

        question.answered();
        questionAnswerRepository.save(answer);
    }

    // 답변 조회
    @Transactional
    public QuestionAnswerDto getAnswer(Long questionId, Long answerId) {
        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new CustomException(CustomErrorResult.NOT_EXIST_QUESTION));

        QuestionAnswer answer = questionAnswerRepository.findById(answerId).orElseThrow(
            () -> new CustomException(CustomErrorResult.NOT_EXIST_ANSWER));

        return QuestionAnswerDto.of(answer);
    }
}