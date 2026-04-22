package com.lxp.course.model.enums;

public enum ContentStatus {
    NORMAL("정상"), HIDDEN("숨김");

    private final String statusName;

    ContentStatus(String statusName) {
        this.statusName = statusName;
    }

    public static ContentStatus from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("콘텐츠 상태는 필수입니다.");
        }
        try {
            return ContentStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바른 콘텐츠 상태를 입력해주세요. (NORMAL / HIDDEN)");
        }
    }
    
}
