package com.lxp.course.model.enums;

public enum ContentType {
    VIDEO("영상"), DOCUMENT("문서");

    private final String typeName;

    ContentType(String typeName) {
        this.typeName = typeName;
    }

    public static ContentType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("콘텐츠 타입은 필수입니다.");
        }
        try {
            return ContentType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바른 콘텐츠 타입을 입력해주세요. (VIDEO / DOCUMENT)");
        }
    }
}
