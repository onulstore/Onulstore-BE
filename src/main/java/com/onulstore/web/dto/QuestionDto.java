package com.onulstore.web.dto;

import com.onulstore.domain.question.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {

    private Long productId;
    private String title;
    private String content;
    private String answerStatus;

    public static QuestionDto of (Question question) {
        return QuestionDto.builder()
                .productId(question.getProduct().getId())
                .title(question.getTitle())
                .content(question.getContent())
                .answerStatus(question.getAnswerState())
                .build();
    }

}
