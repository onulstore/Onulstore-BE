package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.notice.Notice;
import com.onulstore.domain.notice.NoticeRepository;
import com.onulstore.web.dto.NoticeDto;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     *
     * @param noticeRequest
     *
     * @return Notice 등록 정보
     */
    public NoticeDto.NoticeResponse addNotice(NoticeDto.NoticeRequest noticeRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Notice notice = noticeRequest.toNotice();
        return NoticeDto.NoticeResponse.of(noticeRepository.save(notice));
    }

    /**
     * Notice 수정
     *
     * @param noticeRequest
     * @param noticeId
     *
     * @return Notice 수정 정보
     */
    public NoticeDto.NoticeResponse updateNotice(NoticeDto.NoticeRequest noticeRequest,
        Long noticeId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Notice findNotice = noticeRepository.findById(noticeId).orElseThrow(
            () -> new Exception(ErrorResult.NOT_FOUND_NOTICE));

        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }
        Notice notice = findNotice.updateNotice(noticeRequest);
        return NoticeDto.NoticeResponse.of(noticeRepository.save(notice));
    }

    /**
     * 해당 Notice 조회
     *
     * @param noticeId
     *
     * @return Notice 정보
     */
    @Transactional(readOnly = true)
    public NoticeDto.NoticeResponse getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId).map(NoticeDto.NoticeResponse::of)
            .orElseThrow(() -> new Exception(ErrorResult.NOT_FOUND_NOTICE));
    }

    /**
     * 전체 Notice 조회
     *
     * @return 전체 Notice 정보
     */
    @Transactional(readOnly = true)
    public HashMap<String, Object> getNoticeList() {
        HashMap<String, Object> resultMap = new HashMap<>();
        List<Notice> notices = noticeRepository.findAll();
        resultMap.put("notices", notices);
        return resultMap;
    }

    /**
     * 해당 Notice 삭제
     *
     * @param noticeId
     */
    public void deleteNotice(Long noticeId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
            () -> new Exception(ErrorResult.NOT_FOUND_NOTICE));

        noticeRepository.delete(notice);
    }

    public String upload(InputStream inputStream, String originFileName) {
        String s3FileName = UUID.randomUUID() + "-" + originFileName;
        ObjectMetadata objMeta = new ObjectMetadata();
        s3Client.putObject(bucket, s3FileName, inputStream, objMeta);
        return s3FileName;

    }

    public void addImage(Long noticeId, String image) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
            () -> new Exception(ErrorResult.NOT_FOUND_NOTICE));
        notice.insertImage(image);
    }

}
