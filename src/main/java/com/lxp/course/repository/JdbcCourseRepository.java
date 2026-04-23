package com.lxp.course.repository;

import com.lxp.course.model.Course;
import com.lxp.course.model.CourseContent;
import com.lxp.course.model.CourseSection;
import com.lxp.course.model.enums.ContentStatus;
import com.lxp.course.model.enums.ContentType;
import com.lxp.course.model.enums.CourseLevel;
import com.lxp.course.model.enums.CourseStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;

public class JdbcCourseRepository implements CourseRepository {

    private final DataSource dataSource;

    public JdbcCourseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Course save(Course course) {
        String sql = """
                INSERT INTO courses (title, description, instructor_id, status, level,
                    published_at, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setLong(3, course.getInstructorId());
            pstmt.setString(4, course.getStatus().name());
            pstmt.setString(5, course.getLevel().name());
            pstmt.setTimestamp(6, null); // 최초 생성 시 published_at은 null
            pstmt.setTimestamp(7, Timestamp.valueOf(course.getCreatedAt()));
            pstmt.setTimestamp(8, Timestamp.valueOf(course.getUpdatedAt()));

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return Course.reconstruct(
                            keys.getLong(1),
                            course.getTitle(),
                            course.getDescription(),
                            course.getInstructorId(),
                            course.getStatus(),
                            course.getLevel(),
                            null,
                            course.getCreatedAt(),
                            course.getUpdatedAt()
                    );
                }
                throw new RuntimeException("강좌 저장 후 ID를 가져오지 못했습니다.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("강좌 저장 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public Optional<Course> findById(Long id) {
        String sql = """
                SELECT id, title, description, instructor_id, status, level,
                       published_at, created_at, updated_at
                FROM courses
                WHERE id = ?
                """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Course course = Course.reconstruct(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getLong("instructor_id"),
                            CourseStatus.valueOf(rs.getString("status")),
                            CourseLevel.valueOf(rs.getString("level")),
                            rs.getTimestamp("published_at") != null
                                    ? rs.getTimestamp("published_at").toLocalDateTime() : null,
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                    return Optional.of(course);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("강좌 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<Course> findAll() {
        String sql = """
                SELECT id, title, description, instructor_id, status, level,
                       published_at, created_at, updated_at
                FROM courses
                ORDER BY id
                """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Course> courses = new ArrayList<>();
                while (rs.next()) {
                    courses.add(Course.reconstruct(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getLong("instructor_id"),
                            CourseStatus.valueOf(rs.getString("status")),
                            CourseLevel.valueOf(rs.getString("level")),
                            rs.getTimestamp("published_at") != null
                                    ? rs.getTimestamp("published_at").toLocalDateTime() : null,
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    ));
                }
                return courses;
            }

        } catch (SQLException e) {
            throw new RuntimeException("강좌 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public Optional<Course> findWithSectionsAndContentsById(Long id) {
        // SQL 정의: 강좌-섹션-콘텐츠를 한 번에 조회 (1:N:M 관계)
        // - LEFT JOIN: 섹션이나 콘텐츠가 없는 강좌라도 정보를 가져오기 위함
        // - ORDER BY: 동일한 섹션끼리, 동일한 콘텐츠끼리 데이터가 모이도록 정렬함
        String sql = """
                SELECT
                    c.id AS course_id, c.title AS course_title, c.description,
                    c.instructor_id, c.status, c.level, c.published_at,
                    c.created_at AS course_created_at, c.updated_at AS course_updated_at,
                    s.id AS section_id, s.title AS section_title,
                    s.created_at AS section_created_at, s.updated_at AS section_updated_at,
                    ct.id AS content_id, ct.title AS content_title,
                    ct.content_type, ct.status AS content_status,
                    ct.created_at AS content_created_at, ct.updated_at AS content_updated_at
                FROM courses c
                LEFT JOIN course_sections s ON c.id = s.course_id
                LEFT JOIN contents ct ON s.id = ct.section_id
                WHERE c.id = ?
                ORDER BY s.id, ct.id
                """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                Course course = null;
                // 섹션 중복 방지 및 순서 유지를 위해 LinkedHashMap 사용 (Key: SectionID, Value: Section 객체)
                Map<Long, CourseSection> sectionMap = new LinkedHashMap<>();

                while (rs.next()) {
                    // 루트 객체(Course) 초기화
                    // 결과셋의 모든 행에는 동일한 강좌 정보가 포함되어 있으므로, 첫 행에서 딱 한 번만 생성함
                    if (course == null) {
                        course = Course.reconstruct(
                                rs.getLong("course_id"),
                                rs.getString("course_title"),
                                rs.getString("description"),
                                rs.getLong("instructor_id"),
                                CourseStatus.valueOf(rs.getString("status")),
                                CourseLevel.valueOf(rs.getString("level")),
                                rs.getTimestamp("published_at") != null
                                        ? rs.getTimestamp("published_at").toLocalDateTime() : null,
                                rs.getTimestamp("course_created_at").toLocalDateTime(),
                                rs.getTimestamp("course_updated_at").toLocalDateTime()
                        );
                    }

                    // 섹션(Section) 데이터 처리
                    long sectionId = rs.getLong("section_id");
                    // LEFT JOIN 결과로 섹션 정보가 없는 경우(null) 다음 행으로 넘어감
                    if (rs.wasNull()) {
                        continue;
                    }

                    // 이미 Map에 등록된 섹션인지 확인 (하나의 섹션에 여러 콘텐츠가 있으면 행이 여러 개 발생하므로 중복 제거 필요)
                    CourseSection section = sectionMap.get(sectionId);
                    if (section == null) {
                        section = CourseSection.reconstruct(
                                sectionId,
                                rs.getString("section_title"),
                                course, // 섹션이 부모인 Course를 참조하도록 설정
                                getLocalDateTime(rs, "section_created_at"),
                                getLocalDateTime(rs, "section_updated_at")
                        );
                        sectionMap.put(sectionId, section);
                    }

                    // 콘텐츠(Content) 데이터 처리
                    long contentId = rs.getLong("content_id");
                    // 해당 섹션에 콘텐츠가 없는 경우(null), 콘텐츠 생성 단계를 건너뜀
                    if (rs.wasNull()) {
                        continue;
                    }

                    // 콘텐츠 객체 생성 및 현재 섹션에 추가
                    CourseContent content = CourseContent.reconstruct(
                            contentId,
                            rs.getString("content_title"),
                            ContentType.valueOf(rs.getString("content_type")),
                            ContentStatus.valueOf(rs.getString("content_status")),
                            section, // 콘텐츠가 부모인 Section을 참조하도록 설정
                            getLocalDateTime(rs, "content_created_at"),
                            getLocalDateTime(rs, "content_updated_at")
                    );
                    // 섹션 내부 리스트에 콘텐츠 추가 (Reconstruct 전용 메서드 사용)
                    section.addContentForReconstruct(content);
                }

                // 최종 결과 반환 처리
                // 조회된 강좌 자체가 없으면 빈 Optional 반환
                if (course == null) {
                    return Optional.empty();
                }

                // Map에 모아둔 모든 섹션들을 강좌 객체에 최종적으로 연결
                sectionMap.values().forEach(course::addSectionForReconstruct);

                return Optional.of(course);
            }

        } catch (SQLException e) {
            // 예외 발생 시 구체적인 메시지를 담아 RuntimeException으로 전환하여 던짐
            throw new RuntimeException("강좌 상세 조회 중 오류가 발생했습니다.", e);
        }
    }

    private LocalDateTime getLocalDateTime(ResultSet rs, String column) {
        try {
            Timestamp ts = rs.getTimestamp(column);
            return ts != null ? ts.toLocalDateTime() : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Course course) {
        String sql = """
                UPDATE Courses
                SET status = ?, published_at = ?, updated_at = ?
                WHERE id = ?
                """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getStatus().name());
            pstmt.setTimestamp(2, course.getPublishedAt() != null
                    ? Timestamp.valueOf(course.getPublishedAt()) : null);
            pstmt.setTimestamp(3, Timestamp.valueOf(course.getUpdatedAt()));
            pstmt.setLong(4, course.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("강좌 업데이트 중 오류가 발생했습니다.", e);
        }
    }
}