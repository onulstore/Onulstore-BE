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

    private Long memberId;
    private Long productId;
    private String title;
    private String content;
    private String answerStatus;

    public static QuestionDto of (Question question) {
        return QuestionDto.builder()
                .memberId(question.getMember().getId())
                .productId(question.getProduct().getId())
                .title(question.getTitle())
                .content(question.getContent())
                .answerStatus(question.getAnswerState())
                .build();
    }

}
