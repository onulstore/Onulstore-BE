package com.onulstore.web.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class PasswordDto {

    @NotBlank(message = "새 비밀번호를 작성하세요.")
//    @Pattern(regexp = "^(?=.*[a-zA-Z0-9`~!@#$%^&*()\\-_+=\\\\]).{8,15}$", message = "비밀번호는 영문/숫자/특수문자 조합 8자리~15자리로 작성하세요.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호를 다시 한번 작성하세요.")
//    @Pattern(regexp = "^(?=.*[a-zA-Z0-9`~!@#$%^&*()\\-_+=\\\\]).{8,15}$", message = "비밀번호는 영문/숫자/특수문자 조합 8자리~15자리로 작성하세요.")
    private String newPasswordConfirm;

}
