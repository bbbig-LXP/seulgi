package com.lxp.course.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
}