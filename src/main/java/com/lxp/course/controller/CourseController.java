package com.lxp.course.controller;

import com.lxp.course.model.Course;
import com.lxp.course.model.CourseContent;
import com.lxp.course.model.CourseSection;
import com.lxp.course.model.dto.CourseDetailResponse;
import com.lxp.course.model.enums.ContentStatus;
import com.lxp.course.model.enums.ContentType;
import com.lxp.course.model.enums.CourseLevel;
import com.lxp.course.service.CourseService;
import java.util.List;
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

    /**
     * 콘텐츠를 등록하고 콘텐츠 객체를 반환한다.
     */
    public CourseContent createContent(Long sectionId, String title, ContentType type,
            ContentStatus status) {
        return courseService.createContent(sectionId, title, type, status);
    }

    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    public CourseDetailResponse getCourseDetail(Long courseId) {
        Course course = courseService.getCourseWithDetail(courseId);
        return CourseDetailResponse.from(course);
    }

    public Course publishCourse(Long courseId) {
        return courseService.publishCourse(courseId);
    }
}