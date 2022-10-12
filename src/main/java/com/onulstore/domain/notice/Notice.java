package com.onulstore.domain.notice;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.web.dto.NoticeDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Lob
    @Column
    private String content;

    @Column
    private String noticeImg;

    public Notice updateNotice(NoticeDto.NoticeRequest noticeRequest) {
        this.title = noticeRequest.getTitle();
        this.content = noticeRequest.getContent();
        return this;
    }

    public void insertImage(String noticeImg) {
        this.noticeImg = noticeImg;
    }

}