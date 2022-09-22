package com.onulstore.web.controller;

import com.onulstore.service.CurationService;
import com.onulstore.web.dto.CurationDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@RequestMapping("/curations")
@Api(tags = {"Curation-Controller"})
public class CurationController {

    private final CurationService curationService;

    @GetMapping
    @ApiOperation(value = "큐레이션 조회")
    public ResponseEntity<Page<CurationDto.CurationResponse>> getCuration(Pageable pageable) {
        return ResponseEntity.ok(curationService.getCuration(pageable));
    }

    @PostMapping("/magazine")
    @ApiOperation(value = "매거진 등록")
    public ResponseEntity<String> addMagazine(@Valid @RequestBody CurationDto.CurationRequest curationRequest) {
        curationService.createMagazine(curationRequest);
        return ResponseEntity.ok("매거진 등록이 완료되었습니다.");
    }

    @PostMapping("/recommend")
    @ApiOperation(value = "추천제품 등록")
    public ResponseEntity<String> addRecommend(@Valid @RequestBody CurationDto.CurationRequest curationRequest) {
        curationService.createRecommend(curationRequest);
        return ResponseEntity.ok("추천제품 등록이 완료되었습니다.");
    }

    @DeleteMapping("/{curationId}")
    @ApiOperation(value = "큐레이션 상품 삭제")
    public ResponseEntity<String> deleteOne(@PathVariable Long curationId) {
        curationService.deleteCuration(curationId);
        return ResponseEntity.ok("큐레이션 상품이 삭제되었습니다.");
    }

    @GetMapping("/magazine")
    @ApiOperation(value = "매거진 조회")
    public ResponseEntity<Page<CurationDto.CurationResponse>> getMagazine(Pageable pageable) {
        return ResponseEntity.ok(curationService.getMagazine(pageable));
    }

    @GetMapping("/recommend")
    @ApiOperation(value = "추천제품 조회")
    public ResponseEntity<Page<CurationDto.CurationResponse>> getRecommend(Pageable pageable) {
        return ResponseEntity.ok(curationService.getRecommend(pageable));
    }

    @PutMapping("/{curationId}")
    @ApiOperation(value = "큐레이션 수정")
    public ResponseEntity<CurationDto.CurationResponse> updateCuration(
            @RequestBody CurationDto.updateCuration updateCuration, @PathVariable Long curationId) {
        return ResponseEntity.ok(curationService.updateCuration(updateCuration, curationId));
    }

    @PostMapping("/{curationId}/image")
    @ApiOperation(value = "큐레이션 이미지 업로드")
    public ResponseEntity<String> uploadImage(
            @RequestParam("images") MultipartFile multipartFile, @PathVariable Long curationId) throws IOException {
        String image = curationService.upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        curationService.addImage(curationId, image);
        return ResponseEntity.ok("이미지가 등록되었습니다.");
    }

}
