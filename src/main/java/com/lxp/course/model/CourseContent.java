package com.lxp.course.model;

import static com.lxp.course.model.ContentStatus.NORMAL;

import java.time.LocalDateTime;

public class CourseContent {

    private Long id;
    private String title;
    private ContentType type;
    private ContentStatus status;
    private CourseSection section;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CourseContent() {
    }

    private CourseContent(String title, ContentType type, ContentStatus status,
            CourseSection section) {
        validateTitle(title);
        validateType(type);
        validateStatus(status);
        validateSection(section);

        this.title = title;
        this.type = type;
        this.status = status;
        this.section = section;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    /**
     * 새 콘텐츠를 생성한다.
     *
     * <p> 초기 상태는 NORMAL 또는 HIDDEN으로 설정 가능하다.
     */
    public static CourseContent create(String title, ContentType type, ContentStatus status,
            CourseSection section) {
        return new CourseContent(title, type, status, section);
    }

    /**
     * DB 조회 결과로부터 CourseContent 객체를 복원한다.
     *
     * <p> CourseContentRepository에서만 호출되어야 한다. (package-private)
     */
    static CourseContent reconstruct(Long id, String title, ContentType type, ContentStatus status,
            CourseSection section, LocalDateTime createdAt, LocalDateTime updatedAt) {
        CourseContent content = new CourseContent();
        content.id = id;
        content.title = title;
        content.type = type;
        content.status = status;
        content.section = section;
        content.createdAt = createdAt;
        content.updatedAt = updatedAt;
        return content;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("콘텐츠 제목은 필수입니다.");
        }
        if (title.length() < 2 || title.length() > 50) {
            throw new IllegalArgumentException("콘텐츠 제목은 2자 이상 50자 이하여야 합니다.");
        }
    }

    private void validateType(ContentType type) {
        if (type == null) {
            throw new IllegalArgumentException("콘텐츠 타입은 필수입니다.");
        }
    }

    private void validateStatus(ContentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("콘텐츠 상태는 필수입니다.");
        }
    }

    private void validateSection(CourseSection section) {
        if (section == null) {
            throw new IllegalArgumentException("섹션 정보는 필수입니다.");
        }
    }

    /**
     * 학습자에게 노출 가능한 콘텐츠인지 확인한다.
     */
    public boolean isNormal() {
        return this.status == NORMAL;
    }

    public CourseSection getSection() {
        return section;
    }
}
