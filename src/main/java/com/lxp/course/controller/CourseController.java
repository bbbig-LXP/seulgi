package com.lxp.course.controller;

import com.lxp.course.model.Course;
import com.lxp.course.model.CourseSection;
import com.lxp.course.model.enums.CourseLevel;
import com.lxp.course.service.CourseService;
import java.util.Scanner;

public class CourseController {

    private final CourseService courseService;
    private final Scanner scanner;

    public CourseController(CourseService courseService, Scanner scanner) {
        this.courseService = courseService;
        this.scanner = scanner;
    }

    /**
     * 강좌를 등록하고 생성된 강좌 객체를 반환한다.
     */
    public Course createCourse(Long instructorId, String title, String description,
            CourseLevel level) {
        return courseService.createCourse(instructorId, title, description, level);
    }

    /**
     * 섹션을 등록하고 생성된 섹션 객체를 반환한다.
     */
    public CourseSection createSection(Long courseId, String title) {
        return courseService.createSection(courseId, title);
    }

}