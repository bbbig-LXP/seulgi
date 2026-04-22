package com.lxp.course.service;

import com.lxp.course.model.Course;
import com.lxp.course.model.enums.CourseLevel;
import com.lxp.course.repository.CourseRepository;
import com.lxp.user.model.User;
import com.lxp.user.repository.UserRepository;

public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
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
}