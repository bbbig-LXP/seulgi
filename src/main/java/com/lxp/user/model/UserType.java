package com.lxp.user.model;

public enum UserType {
    STUDENT("수강생"), INSTRUCTOR("강사"), ADMIN("관리자");

    private final String typeName;

    UserType(String typeName) {
        this.typeName = typeName;
    }
}
