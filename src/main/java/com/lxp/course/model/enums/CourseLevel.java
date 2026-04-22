package com.lxp.course.model.enums;

public enum CourseLevel {
    BEGINNER("입문"), INTERMEDIATE("중급"), ADVANCED("고급");

    private final String levelName;

    CourseLevel(String levelName) {
        this.levelName = levelName;
    }

    public static CourseLevel from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("난이도는 필수입니다.");
        }

        try {
            return CourseLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "올바른 난이도를 입력해주세요. (BEGINNER / INTERMEDIATE / ADVANCED)");
        }
    }
}
