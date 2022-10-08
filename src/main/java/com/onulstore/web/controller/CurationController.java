package com.onulstore.web.controller;

import com.onulstore.domain.curation.CurationProduct;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@RequestMapping("/curations")
@Api(tags = {"Curation-Controller"})
public class CurationController {

    private final CurationService curationService;

    @GetMapping
    @ApiOperation(value = "전체 큐레이션 조회")
    public ResponseEntity<Page<CurationDto.CurationInfo>> getCuration(Pageable pageable) {
        return ResponseEntity.ok(curationService.getCuration(pageable));
    }

    @GetMapping("/{curationId}")
    @ApiOperation(value = "특정 큐레이션 조회")
    public ResponseEntity<CurationDto.CurationInfo> getCuration(@PathVariable Long curationId) {
        return ResponseEntity.ok(curationService.getCurationList(curationId));
    }

    @PostMapping("/magazine")
    @ApiOperation(value = "매거진 등록")
    public ResponseEntity<String> addMagazine(
        @Valid @RequestBody CurationDto.MagazineRequest magazineRequest) {
        curationService.createMagazine(magazineRequest);
        return ResponseEntity.ok("매거진 등록이 완료되었습니다.");
    }

    @PostMapping("/recommend")
    @ApiOperation(value = "추천제품 등록")
    public ResponseEntity<String> addRecommend(
        @Valid @RequestBody CurationDto.RecommendRequest recommendRequest) {
        curationService.createRecommend(recommendRequest);
        return ResponseEntity.ok("추천제품 등록이 완료되었습니다.");
    }

    @DeleteMapping("/{curationId}")
    @ApiOperation(value = "큐레이션 삭제")
    public ResponseEntity<String> deleteCuration(@PathVariable Long curationId) {
        curationService.deleteCuration(curationId);
        return ResponseEntity.ok("큐레이션 상품이 삭제되었습니다.");
    }

    @GetMapping("/magazine")
    @ApiOperation(value = "매거진 전체 조회")
    public ResponseEntity<Page<CurationDto.CurationInfo>> getMagazine(Pageable pageable) {
        return ResponseEntity.ok(curationService.getMagazine(pageable));
    }

    @GetMapping("/recommend")
    @ApiOperation(value = "추천제품 전체 조회")
    public ResponseEntity<Page<CurationDto.CurationInfo>> getRecommend(Pageable pageable) {
        return ResponseEntity.ok(curationService.getRecommend(pageable));
    }

    @PutMapping("/{curationId}")
    @ApiOperation(value = "큐레이션 수정")
    public ResponseEntity<CurationDto.CurationResponse> updateCuration(
        @RequestBody CurationDto.UpdateCuration updateCuration, @PathVariable Long curationId) {
        return ResponseEntity.ok(curationService.updateCuration(updateCuration, curationId));
    }

    @PostMapping("/{curationId}/image")
    @ApiOperation(value = "큐레이션 이미지 업로드")
    public ResponseEntity<String> uploadImage(@RequestParam("images") MultipartFile multipartFile,
        @PathVariable Long curationId) throws IOException {
        curationService.uploadImage(multipartFile, curationId);
        return ResponseEntity.ok("이미지가 등록되었습니다.");
    }

    @PutMapping("/{curationId}/display")
    @ApiOperation(value = "공개 여부 TRUE")
    public ResponseEntity<String> display(@PathVariable Long curationId) {
        curationService.display(curationId);
        return ResponseEntity.ok("해당 큐레이션이 공개 처리 되었습니다");
    }

    @PutMapping("/{curationId}/unDisplay")
    @ApiOperation(value = "공개 여부 FALSE")
    public ResponseEntity<String> unDisplay(@PathVariable Long curationId) {
        curationService.unDisplay(curationId);
        return ResponseEntity.ok("해당 큐레이션이 비공개 처리 되었습니다");
    }

}
