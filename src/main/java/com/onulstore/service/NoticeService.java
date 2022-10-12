package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.CustomErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.notice.Notice;
import com.onulstore.domain.notice.NoticeRepository;
import com.onulstore.web.dto.NoticeDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client s3Client;
    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;

    /**
     * Notice 등록
     * @param noticeRequest
     * @return Notice 등록 정보
     */
    public NoticeDto.NoticeResponse addNotice(NoticeDto.NoticeRequest noticeRequest) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        Notice notice = noticeRequest.toNotice();
        return NoticeDto.NoticeResponse.of(noticeRepository.save(notice));
    }

    /**
     * Notice 수정
     * @param noticeRequest
     * @param noticeId
     * @return Notice 수정 정보
     */
    public NoticeDto.NoticeResponse updateNotice(NoticeDto.NoticeRequest noticeRequest,
        Long noticeId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }
        Notice findNotice = noticeRepository.findById(noticeId).orElseThrow(
            () -> new CustomException(CustomErrorResult.NOT_FOUND_NOTICE));

        Notice notice = findNotice.updateNotice(noticeRequest);
        return NoticeDto.NoticeResponse.of(noticeRepository.save(notice));
    }

    /**
     * 해당 Notice 조회
     * @param noticeId
     * @return Notice 정보
     */
    @Transactional(readOnly = true)
    public NoticeDto.NoticeResponse getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId).map(NoticeDto.NoticeResponse::of)
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_FOUND_NOTICE));
    }

    /**
     * 전체 Notice 조회
     * @return 전체 Notice 정보
     */
    @Transactional(readOnly = true)
    public Map<String, List<Notice>> getNoticeList() {
        Map<String, List<Notice>> resultMap = new HashMap<>();
        List<Notice> notices = noticeRepository.findAll();
        resultMap.put("notices", notices);
        return resultMap;
    }

    /**
     * 해당 Notice 삭제
     * @param noticeId
     */
    public void deleteNotice(Long noticeId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
            () -> new CustomException(CustomErrorResult.NOT_FOUND_NOTICE));

        noticeRepository.delete(notice);
    }

    public String uploadContent(MultipartFile multipartFile, Long noticeId) throws IOException {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
            () -> new CustomException(CustomErrorResult.NOT_FOUND_NOTICE));

        InputStream inputStream = multipartFile.getInputStream();
        String originFileName = multipartFile.getOriginalFilename();
        String s3FileName = UUID.randomUUID() + "-" + originFileName;
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        s3Client.putObject(bucket, s3FileName, inputStream, objMeta);
        notice.insertImage(s3FileName);
        return s3FileName;
    }

}
