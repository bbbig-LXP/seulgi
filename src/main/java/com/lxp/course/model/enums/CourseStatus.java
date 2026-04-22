package com.lxp.course.model.enums;

public enum CourseStatus {
    DRAFT("예정"), PUBLISHED("발행"), ARCHIVED("보관");

    private final String statusName;

    CourseStatus(String statusName) {
        this.statusName = statusName;
    }
}
