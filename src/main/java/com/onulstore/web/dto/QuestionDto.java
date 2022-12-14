package com.onulstore.web.dto;

import com.onulstore.domain.question.Question;
import com.onulstore.domain.questionAnswer.QuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class QuestionDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuestionRequest {

        private Long productId;
        private String title;
        private String content;
        private boolean secret;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuestionResponse {

        private Long questionId;
        private Long productId;
        private String title;
        private String content;
        private boolean secret;
        private boolean answerStatus;

        public static QuestionResponse of(Question question) {
            return QuestionResponse.builder()
                .questionId(question.getId())
                .productId(question.getProduct().getId())
                .title(question.getTitle())
                .content(question.getContent())
                .secret(question.isSecret())
                .answerStatus(question.isAnswerStatus())
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuestionRes {

        private Long questionId;
        private Long productId;
        private String title;
        private String content;
        private boolean secret;
        private boolean answerStatus;
        private QuestionAnswer questionAnswer;

        public static QuestionDto.QuestionRes of(Question question, QuestionAnswer questionAnswer) {
            return QuestionRes.builder()
                .questionId(question.getId())
                .productId(question.getProduct().getId())
                .title(question.getTitle())
                .content(question.getContent())
                .secret(question.isSecret())
                .answerStatus(question.isAnswerStatus())
                .questionAnswer(questionAnswer)
                .build();
        }
    }
}
