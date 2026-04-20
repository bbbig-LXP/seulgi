package com.lxp.course.model;

import static com.lxp.course.model.CourseStatus.DRAFT;

import com.lxp.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Course {

    private Long id;
    private String title;
    private String description;
    private Long instructorId;
    private CourseStatus status;
    private CourseLevel level;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private final List<CourseSection> sections = new ArrayList<>();

    private Course() {
    }

    private Course(String title, String description, User instructor, CourseLevel level) {
        validateTitle(title);
        validateDescription(description);
        validateInstructor(instructor);
        validateLevel(level);

        this.title = title;
        this.description = description;
        this.instructorId = instructor.getId();
        this.status = DRAFT;
        this.level = level;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    /**
     * 새 강좌를 생성한다.
     *
     * <p> 생성자에서 도메인 규칙을 검증한 후 객체를 생성한다.
     * <p> 최초 생성 시 상태는 항상 DRAFT이다.
     */
    public static Course create(String title, String description,
            User instructor, CourseLevel level) {
        return new Course(title, description, instructor, level);
    }

    /**
     * DB 조회 결과로부터 Course 객체를 복원한다.
     *
     * <p> 이미 저장된 데이터를 복원하는 것이므로 도메인 검증을 수행하지 않는다.
     * <p> CourseRepository에서만 호출되어야 한다. (package-private)
     */
    static Course reconstruct(Long id, String title, String description, Long instructorId,
            CourseStatus status, CourseLevel level, LocalDateTime publishedAt,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        Course course = new Course();
        course.id = id;
        course.title = title;
        course.description = description;
        course.instructorId = instructorId;
        course.status = status;
        course.level = level;
        course.publishedAt = publishedAt;
        course.createdAt = createdAt;
        course.updatedAt = updatedAt;
        return course;
    }
    
    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("강좌명은 필수입니다.");
        }

        if (title.length() < 2 || title.length() > 50) {
            throw new IllegalArgumentException("강좌명은 2자 이상 50자 이하여야 합니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("강좌 설명은 필수입니다.");
        }
        if (description.length() < 2 || description.length() > 200) {
            throw new IllegalArgumentException("강좌 설명은 2자 이상 200자 이하여야 합니다.");
        }
    }

    private void validateInstructor(User instructor) {
        if (instructor == null) {
            throw new IllegalArgumentException("강사 정보는 필수입니다.");
        }
        if (!instructor.isInstructor()) {
            throw new IllegalArgumentException("강좌는 강사만 등록할 수 있습니다.");
        }
    }

    private void validateLevel(CourseLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("강좌 난이도는 필수입니다.");
        }
    }

}
