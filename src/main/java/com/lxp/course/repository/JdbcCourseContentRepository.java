package com.lxp.course.repository;

import com.lxp.course.model.CourseContent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import javax.sql.DataSource;

public class JdbcCourseContentRepository implements CourseContentRepository {

    private final DataSource dataSource;

    public JdbcCourseContentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CourseContent save(CourseContent content) {
        String sql = """
                INSERT INTO contents (section_id, title, content_type, status, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, content.getSection().getId());
            pstmt.setString(2, content.getTitle());
            pstmt.setString(3, content.getType().name());
            pstmt.setString(4, content.getStatus().name());
            pstmt.setTimestamp(5, Timestamp.valueOf(content.getCreatedAt()));
            pstmt.setTimestamp(6, Timestamp.valueOf(content.getUpdatedAt()));

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return CourseContent.reconstruct(
                            keys.getLong(1),
                            content.getTitle(),
                            content.getType(),
                            content.getStatus(),
                            content.getSection(),
                            content.getCreatedAt(),
                            content.getUpdatedAt()
                    );
                }
                throw new RuntimeException("콘텐츠 저장 후 ID를 가져오지 못했습니다.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("콘텐츠 저장 중 오류가 발생했습니다.", e);
        }
    }
}