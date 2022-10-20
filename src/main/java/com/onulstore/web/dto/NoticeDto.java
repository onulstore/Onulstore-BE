package com.onulstore.web.dto;

import com.onulstore.domain.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NoticeDto {

    @Getter
    @Builder
    public static class NoticeRequest {

        private String title;
        private String content;

        public Notice toNotice() {
            return Notice.builder()
                    .title(title)
                    .content(content)
                    .build();
        }

    }

    @Getter
    @Builder
    public static class NoticeResponse {

        private Long id;
        private String title;
        private String content;
        private String noticeImg;

        public static NoticeResponse of(Notice notice) {
            return NoticeResponse.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .noticeImg(notice.getNoticeImg())
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BannerRequest {

        private String content;

        public Notice toBanner() {
            return Notice.builder()
                    .title("Home Banner")
                    .content(content)
                    .build();
        }

    }


}
