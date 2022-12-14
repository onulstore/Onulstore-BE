package com.onulstore.web.dto;

import com.onulstore.domain.questionAnswer.QuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionAnswerDto {

    private Long questionId;
    private String answer;

    public static QuestionAnswerDto of (QuestionAnswer questionAnswer) {
        return QuestionAnswerDto.builder()
                .questionId(questionAnswer.getQuestion().getId())
                .answer(questionAnswer.getAnswer())
                .build();
    }
}
