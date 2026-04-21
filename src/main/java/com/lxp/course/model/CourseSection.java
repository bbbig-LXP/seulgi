package com.lxp.course.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseSection {

    private Long id;
    private String title;
    private Course course;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CourseContent> contents = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public List<CourseContent> getContents() {
        return Collections.unmodifiableList(contents);
    }

}
