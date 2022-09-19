package com.onulstore.web.controller;

import com.onulstore.service.QuestionService;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.QuestionDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Question Controller"})
public class QuestionController {

    private final QuestionService questionService;

    // 질문 등록
    @ApiOperation(value = "질문 등록")
    @PostMapping("/questions")
    public ResponseEntity<String> insetQuestion(@RequestBody QuestionDto questionDto) {
        questionService.insertQuestion(questionDto);
        return ResponseEntity.ok("해당 질문이 등록되었습니다.");
    }

    // 질문 수정
    @ApiOperation(value = "질문 수정")
    @PutMapping("/questions/{questionId}")
    public ResponseEntity<String> updateQuestion(@PathVariable Long questionId,@RequestBody QuestionDto questionDto) {
        questionService.updateQuestion(questionId, questionDto);
        return ResponseEntity.ok("해당 질문이 수정되었습니다.");
    }

    // 질문 삭제
    @ApiOperation(value = "질문 삭제")
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok("해당 질문이 삭제되었습니다.");
    }

}
