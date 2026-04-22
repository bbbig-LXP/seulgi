package com.lxp.course.model;

import static com.lxp.course.model.enums.CourseStatus.ARCHIVED;
import static com.lxp.course.model.enums.CourseStatus.DRAFT;
import static com.lxp.course.model.enums.CourseStatus.PUBLISHED;

import com.lxp.course.model.enums.CourseLevel;
import com.lxp.course.model.enums.CourseStatus;
import com.lxp.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
     */
    public static Course reconstruct(Long id, String title, String description, Long instructorId,
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

    /**
     * 강좌의 상태를 발행 상태로 변경한다. (publish)
     *
     * <p>- DRAFT 상태인 강좌만 발행할 수 있다.
     * <p>- 섹션이 최소 1개 이상 존재해야 한다.
     * <p>- 각 섹션 내 NORMAL 상태의 콘텐츠가 최소 1개 이상 존재해야 한다.
     * <p>- 검증 통과 시 status를 PUBLISHED로 바꾸고, publishedAt에 현재 시각을 기록한다.
     */
    public void publish() {
        if (this.status != DRAFT) {
            throw new IllegalStateException("DRAFT 상태인 강좌만 발행할 수 있습니다.");
        }

        if (sections.isEmpty()) {
            throw new IllegalStateException("강좌에 섹션이 최소 1개 이상 존재해야 합니다.");
        }

        for (CourseSection section : sections) {
            boolean hasNoNormalContent = section.getContents().stream()
                    .noneMatch(CourseContent::isNormal);

            if (hasNoNormalContent) {
                throw new IllegalStateException(
                        String.format("섹션 '%s'에 NORMAL 상태의 콘텐츠가 최소 1개 이상 존재해야 합니다.",
                                section.getTitle()));
            }
        }

        this.status = PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 섹션을 추가한다.
     *
     * <p>- 강좌의 상태가 DRAFT여야 한다.
     */
    public void addSection(CourseSection section) {
        if (section == null) {
            throw new IllegalArgumentException("추가하려는 섹션이 null입니다.");
        }
        if (this.status != DRAFT) {
            throw new IllegalStateException("DRAFT 상태인 강좌에만 섹션을 추가할 수 있습니다.");
        }

        this.sections.add(section);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 발행된 강좌인지 확인한다.
     */
    public boolean isPublished() {
        return this.status == PUBLISHED;
    }

    /**
     * 예정 상태인 강좌인지 확인한다.
     */
    public boolean isDraft() {
        return this.status == DRAFT;
    }

    /**
     * 보관된 강좌인지 확인한다.
     */
    public boolean isArchived() {
        return this.status == ARCHIVED;
    }

    /**
     * 강좌를 보관 상태로 변경한다.
     *
     * <p>- PUBLISHED 상태인 강좌만 보관할 수 있다.
     */
    public void archive() {
        if (this.status != PUBLISHED) {
            throw new IllegalStateException("PUBLISHED 상태인 강좌만 보관할 수 있습니다.");
        }
        this.status = ARCHIVED;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<CourseSection> getSections() {
        return Collections.unmodifiableList(sections);
    }
}