package com.sparta.lv3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 소문자와 숫자 포함 4~10이내로 입력해주세요.")
    private String username;

    @NotBlank
    // 정규식 뒤에 [A-Za-z\d~!@#$%^&*()+|=]를 붙여줘야 에러가 안남
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,15}$", message = "비밀번호는 대소문자와 숫자, 특수문자 포함 8~15자리 이내로 입력해주세요.")
    private String password;
    private String adminToken = "";
}
