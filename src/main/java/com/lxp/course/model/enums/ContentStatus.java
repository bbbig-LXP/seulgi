package com.lxp.course.model.enums;

public enum ContentStatus {
    NORMAL("정상"), HIDDEN("숨김");

    private final String statusName;

    ContentStatus(String statusName) {
        this.statusName = statusName;
    }
}
