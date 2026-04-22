package com.lxp.course.model.dto;

import com.lxp.course.model.Course;
import com.lxp.course.model.CourseSection;
import java.util.List;

public record CourseDetailResponse(
        Course course,
        List<CourseSection> sections
) {

    public static CourseDetailResponse from(Course course) {
        return new CourseDetailResponse(course, course.getSections());
    }

}
