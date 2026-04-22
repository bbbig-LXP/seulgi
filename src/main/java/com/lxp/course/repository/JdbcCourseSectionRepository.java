package com.lxp.course.repository;

import com.lxp.course.model.CourseSection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
}