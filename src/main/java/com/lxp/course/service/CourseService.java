package com.lxp.course.service;

import com.lxp.course.model.Course;
import com.lxp.course.model.CourseContent;
import com.lxp.course.model.CourseSection;
import com.lxp.course.model.enums.ContentStatus;
import com.lxp.course.model.enums.ContentType;
import com.lxp.course.model.enums.CourseLevel;
import com.lxp.course.repository.CourseContentRepository;
import com.lxp.course.repository.CourseRepository;
import com.lxp.course.repository.CourseSectionRepository;
import com.lxp.user.model.User;
import com.lxp.user.repository.UserRepository;
import java.util.List;

public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseSectionRepository sectionRepository;
    private final CourseContentRepository contentRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository,
            CourseSectionRepository sectionRepository, CourseContentRepository contentRepository,
            UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.contentRepository = contentRepository;
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
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId=" + instructorId));

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

        course.addSection(section);

        return sectionRepository.save(section);
    }

    /**
     * 콘텐츠를 등록한다.
     *
     * <p>- section_id가 DB에 존재해야 한다. (인프라 검증)
     * <p>- 강좌 상태가 DRAFT 또는 PUBLISHED여야 한다. (CourseSection 도메인에서 검증)
     * <p>- 콘텐츠 제목 불변식은 CourseContent 도메인에서 검증한다.
     */
    public CourseContent createContent(Long sectionId, String title, ContentType type,
            ContentStatus status) {

        CourseSection section = sectionRepository.findById(sectionId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 섹션입니다. sectionId=" + sectionId));

        CourseContent content = CourseContent.create(title, type, status, section);

        section.addContent(content);

        return contentRepository.save(content);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseWithDetail(Long courseId) {
        return courseRepository.findWithSectionsAndContentsById(courseId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 강좌입니다. courseId=" + courseId));
    }

    public Course publishCourse(Long courseId) {
        Course course = courseRepository.findWithSectionsAndContentsById(courseId)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 강좌입니다. courseId=" + courseId));

        course.publish();

        courseRepository.update(course);

        return course;
    }
}