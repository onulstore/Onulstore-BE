package com.onulstore.web.dto;

import com.onulstore.domain.question.Question;
import com.onulstore.domain.questionAnswer.QuestionAnswer;
import com.onulstore.web.dto.QuestionDto.QuestionResponse;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
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
        @ApiModelProperty(value = "비밀글여부", example = "Y/N")
        private Character secret;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuestionResponse {

        private Long productId;
        private String title;
        private String content;
        private Character secret;
        private boolean answerStatus;

        public static QuestionResponse of(Question question) {
            return QuestionResponse.builder()
                .productId(question.getProduct().getId())
                .title(question.getTitle())
                .content(question.getContent())
                .secret(question.getSecret())
                .answerStatus(question.isAnswerStatus())
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuestionRes {

        private Long productId;
        private String title;
        private String content;
        private Character secret;
        private boolean answerStatus;
        private QuestionAnswer questionAnswer;

        public static QuestionDto.QuestionRes of(Question question, QuestionAnswer questionAnswer) {
            return QuestionDto.QuestionRes.builder()
                .productId(question.getProduct().getId())
                .title(question.getTitle())
                .content(question.getContent())
                .secret(question.getSecret())
                .answerStatus(question.isAnswerStatus())
                .questionAnswer(questionAnswer)
                .build();
        }
    }
}
