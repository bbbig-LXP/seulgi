package com.lxp.course.model;

import static com.lxp.course.model.ContentStatus.NORMAL;

public class CourseContent {

    private Long id;
    private String title;
    private ContentStatus status;
    private ContentType type;

    public boolean isNormal() {
        return this.status == NORMAL;
    }
    
}
