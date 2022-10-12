package com.onulstore.web.controller;

import com.onulstore.service.QuestionAnswerService;
import com.onulstore.web.dto.QuestionAnswerDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"QuestionAnswer-Controller"})
public class QuestionAnswerController {

    private final QuestionAnswerService questionAnswerService;

    // 답변 등록
    @ApiOperation(value = "답변 등록 / 인증 필요(관리자)")
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<String> insertAnswer(@PathVariable Long questionId,
        @RequestBody QuestionAnswerDto questionAnswerDto) {
        questionAnswerService.insertAnswer(questionId, questionAnswerDto);
        return ResponseEntity.ok("해당 질문에 답변이 등록되었습니다.");
    }

    // 답변 조회
    @ApiOperation(value = "답변 조회 / 인증 필요")
    @GetMapping("/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<QuestionAnswerDto> getAnswer(@PathVariable Long questionId,
        @PathVariable Long answerId) {
        return ResponseEntity.ok(questionAnswerService.getAnswer(questionId, answerId));
    }
}