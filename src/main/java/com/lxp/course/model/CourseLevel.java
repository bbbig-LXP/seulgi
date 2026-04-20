package com.lxp.course.model;

public enum CourseLevel {
    BEGINNER("입문자"), INTERMEDIATE("중급"), ADVANCED("고급");

    private final String levelName;

    CourseLevel(String levelName) {
        this.levelName = levelName;
    }
}
