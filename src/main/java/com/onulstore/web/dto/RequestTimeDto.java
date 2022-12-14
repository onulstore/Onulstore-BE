package com.onulstore.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onulstore.domain.enums.OrderStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestTimeDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime localDateTime;

    private OrderStatus orderStatus;

}
