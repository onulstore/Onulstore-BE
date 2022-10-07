package com.onulstore.web.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class PasswordDto {

    @NotBlank(message = "새 비밀번호를 작성하세요.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호를 다시 한번 작성하세요.")
    private String newPasswordConfirm;

}
