package com.lxp.course.repository;

import com.lxp.course.model.Course;
import com.lxp.course.model.CourseSection;
import com.lxp.course.model.enums.CourseLevel;
import com.lxp.course.model.enums.CourseStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import javax.sql.DataSource;

public class JdbcCourseSectionRepository implements CourseSectionRepository {

    private final DataSource dataSource;

    public JdbcCourseSectionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CourseSection save(CourseSection section) {
        String sql = """
                INSERT INTO course_sections (course_id, title, created_at, updated_at)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, section.getCourse().getId());
            pstmt.setString(2, section.getTitle());
            pstmt.setTimestamp(3, Timestamp.valueOf(section.getCreatedAt()));
            pstmt.setTimestamp(4, Timestamp.valueOf(section.getUpdatedAt()));

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return CourseSection.reconstruct(
                            keys.getLong(1),
                            section.getTitle(),
                            section.getCourse(),
                            section.getCreatedAt(),
                            section.getUpdatedAt()
                    );
                }
                throw new RuntimeException("섹션 저장 후 ID를 가져오지 못했습니다.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("섹션 저장 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public Optional<CourseSection> findById(Long id) {
        String sql = """
                SELECT
                    s.id AS section_id, s.title AS section_title,
                    s.created_at AS section_created_at, s.updated_at AS section_updated_at,
                    c.id AS course_id, c.title AS course_title, c.description,
                    c.instructor_id, c.status, c.level, c.published_at,
                    c.created_at AS course_created_at, c.updated_at AS course_updated_at
                FROM CourseSections s
                JOIN Courses c ON s.course_id = c.id
                WHERE s.id = ?
                """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Course course = Course.reconstruct(
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

                    CourseSection section = CourseSection.reconstruct(
                            rs.getLong("section_id"),
                            rs.getString("section_title"),
                            course,
                            rs.getTimestamp("section_created_at").toLocalDateTime(),
                            rs.getTimestamp("section_updated_at").toLocalDateTime()
                    );

                    return Optional.of(section);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("섹션 조회 중 오류가 발생했습니다.", e);
        }
    }
}