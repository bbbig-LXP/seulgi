package com.lxp.course.model.enums;

public enum CourseLevel {
    BEGINNER("입문"), INTERMEDIATE("중급"), ADVANCED("고급");

    private final String levelName;

    CourseLevel(String levelName) {
        this.levelName = levelName;
    }
}
