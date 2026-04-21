package com.lxp.course.model;

public enum ContentType {
    VIDEO("영상"), DOCUMENT("문서");

    private final String typeName;

    ContentType(String typeName) {
        this.typeName = typeName;
    }
}
