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

public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
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

    /**
     * 모든 강좌를 조회한다.
     */
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    /**
     * 강좌의 상세 정보를 조회한다. 강좌에 속한 섹션과 컨텐츠를 모두 반환한다.
     */
    public CourseDetailResponse getCourseDetail(Long courseId) {
        Course course = courseService.getCourseWithDetail(courseId);
        return CourseDetailResponse.from(course);
    }

    /**
     * 강좌를 발행한다.
     */
    public Course publishCourse(Long courseId) {
        return courseService.publishCourse(courseId);
    }

    public Course archiveCourse(Long courseId) {
        return courseService.archiveCourse(courseId);
    }
}