package com.sparta.lv2.entity;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    USER(Authority.USER), // 사용자
    ADMIN(Authority.ADMIN); // 관리자

    private final String authority;

    // authority 생성자 주입
    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
