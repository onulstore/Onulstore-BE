package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.question.QuestionRepository;
import com.onulstore.domain.questionAnswer.QuestionAnswer;
import com.onulstore.domain.questionAnswer.QuestionAnswerRepository;
import com.onulstore.exception.UserException;
import com.onulstore.web.dto.QuestionAnswerDto;
import lombok.RequiredArgsConstructor;
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
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new UserException(UserErrorResult.NOT_EXIST_QUESTION));

        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        QuestionAnswer answer = QuestionAnswer.builder()
                .member(member)
                .question(question)
                .answer(questionAnswerDto.getAnswer())
                .build();

        questionAnswerRepository.save(answer);
        question.Answered();
    }

    // 답변 조회
    @Transactional
    public QuestionAnswerDto getAnswer(Long questionId, Long answerId) {
        Question question = questionRepository.findById(questionId).orElseThrow(
            () -> new UserException(UserErrorResult.NOT_EXIST_QUESTION));

        QuestionAnswer answer = questionAnswerRepository.findById(answerId).orElseThrow(
            () -> new UserException(UserErrorResult.NOT_EXIST_ANSWER));

        return QuestionAnswerDto.of(answer);
    }
}