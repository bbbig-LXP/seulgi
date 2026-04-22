package com.lxp.global.util;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final Dotenv dotenv = Dotenv.load(); // .env 파일 로드

    private static String requireEnv(String key) throws SQLException {
        String value = dotenv.get(key);

        if (value == null || value.isBlank()) {
            throw new SQLException("필수 환경변수 누락: " + key);
        }

        return value;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 필요 시 클래스 로딩 (구형 환경 대비)

            String URL = requireEnv("DB_URL");
            String USER = requireEnv("DB_USER");
            String PASSWORD = requireEnv("DB_PASSWORD");

            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC 드라이버를 찾을 수 없습니다.", e);
        }
    }

    public static String getEnv(String key) throws SQLException {
        return requireEnv(key);
    }
}
