package com.onulstore.web.controller;

import com.onulstore.domain.notice.Notice;
import com.onulstore.service.NoticeService;
import com.onulstore.web.dto.NoticeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
@Api(tags = {"Notice-Controller"})
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @ApiOperation(value = "공지 등록 / 인증 필요(관리자)")
    public ResponseEntity<NoticeDto.NoticeResponse> addNotice(
        @RequestBody NoticeDto.NoticeRequest noticeRequest) {
        return ResponseEntity.ok(noticeService.addNotice(noticeRequest));
    }

    @PutMapping("/{noticeId}")
    @ApiOperation(value = "공지 수정 / 인증 필요(관리자)")
    public ResponseEntity<NoticeDto.NoticeResponse> updateNotice(
        @RequestBody NoticeDto.NoticeRequest noticeRequest, @PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.updateNotice(noticeRequest, noticeId));
    }

    @GetMapping("/{noticeId}")
    @ApiOperation(value = "해당 공지 조회")
    public ResponseEntity<NoticeDto.NoticeResponse> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    @GetMapping
    @ApiOperation(value = "전체 공지 조회")
    public ResponseEntity<Map<String, List<Notice>>> getNoticeList() {
        return ResponseEntity.ok(noticeService.getNoticeList());
    }

    @DeleteMapping("/{noticeId}")
    @ApiOperation(value = "공지 삭제 / 인증 필요(관리자)")
    public ResponseEntity<String> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok("공지가 삭제되었습니다.");
    }

    @PostMapping("/{noticeId}/image")
    @ApiOperation(value = "공지 이미지 업로드 / 인증 필요(관리자)")
    public ResponseEntity<String> uploadImage(@RequestParam("images") MultipartFile multipartFile,
        @PathVariable Long noticeId) throws IOException {
        noticeService.uploadImage(multipartFile, noticeId);
        return ResponseEntity.ok("이미지가 등록되었습니다.");
    }

}
