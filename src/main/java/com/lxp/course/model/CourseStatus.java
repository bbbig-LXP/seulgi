package com.lxp.course.model;

public enum CourseStatus {
    DRAFT("예정"), PUBLISHED("공개");

    private final String statusName;

    CourseStatus(String statusName) {
        this.statusName = statusName;
    }
}
