package com.lxp.course.service;

import com.lxp.course.model.Course;
import com.lxp.course.model.CourseSection;
import com.lxp.course.model.enums.CourseLevel;
import com.lxp.course.repository.CourseRepository;
import com.lxp.course.repository.CourseSectionRepository;
import com.lxp.user.model.User;
import com.lxp.user.repository.UserRepository;

public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseSectionRepository sectionRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository,
            CourseSectionRepository sectionRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
    }

    /**
     * 강좌를 등록한다.
     *
     * <p>- 등록하는 사용자가 DB에 존재해야 한다. (인프라 검증)
     * <p>- 등록하는 사용자의 type은 INSTRUCTOR여야 한다. (Course 도메인에서 검증)
     * <p>- title, description, level 불변식은 Course 도메인에서 검증한다.
     * <p>- 최초 생성 시 status는 DRAFT로 강제된다. (Course 도메인에서 강제)
     */
    public Course createCourse(Long instructorId, String title, String description,
            CourseLevel level) {
        User instructor = userRepository.findById(instructorId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + instructorId));

        Course course = Course.create(title, description, instructor, level);

        return courseRepository.save(course);
    }

    /**
     * 섹션을 등록한다.
     *
     * <p>- course_id가 DB에 존재해야 한다. (인프라 검증)
     * <p>- 참조되는 강좌의 상태가 DRAFT여야 한다. (Course 도메인에서 검증)
     * <p>- 섹션 제목 불변식은 CourseSection 도메인에서 검증한다.
     */
    public CourseSection createSection(Long courseId, String title) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 강좌입니다. courseId = " + courseId));

        CourseSection section = CourseSection.create(title, course);

        course.addSection(section); // DRAFT 상태 검증

        return sectionRepository.save(section);
    }

}