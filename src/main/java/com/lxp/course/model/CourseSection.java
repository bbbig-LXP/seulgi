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

    private final List<CourseContent> contents = new ArrayList<>();

    private CourseSection() {
    }

    private CourseSection(String title, Course course) {
        validateTitle(title);
        validateCourse(course);

        this.title = title;
        this.course = course;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    /**
     * 새 섹션을 생성한다.
     *
     * <p> 참조되는 강좌의 상태가 DRAFT여야 한다.
     */
    public static CourseSection create(String title, Course course) {
        return new CourseSection(title, course);
    }

    /**
     * DB 조회 결과로부터 CourseSection 객체를 복원한다.
     *
     * <p> CourseSectionRepository에서만 호출되어야 한다. (package-private)
     */
    static CourseSection reconstruct(Long id, String title, Course course,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        CourseSection section = new CourseSection();
        section.id = id;
        section.title = title;
        section.course = course;
        section.createdAt = createdAt;
        section.updatedAt = updatedAt;
        return section;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("섹션 제목은 필수입니다.");
        }

        if (title.length() > 50) {
            throw new IllegalArgumentException("섹션 제목은 50자 이하여야 합니다.");
        }
    }

    private void validateCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("강좌 정보는 필수입니다.");
        }
        if (!course.isDraft()) {
            throw new IllegalStateException("DRAFT 상태인 강좌에만 섹션을 추가할 수 있습니다.");
        }
    }

    /**
     * 콘텐츠를 추가한다.
     */
    public void addContent(CourseContent content) {
        if (content == null) {
            throw new IllegalArgumentException("콘텐츠 정보는 필수입니다.");
        }
        
        this.contents.add(content);
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Course getCourse() {
        return course;
    }

    public List<CourseContent> getContents() {
        return Collections.unmodifiableList(contents);
    }

}
