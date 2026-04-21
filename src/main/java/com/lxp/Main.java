package com.lxp;

import com.lxp.global.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("DB 연결 성공: " + conn.getMetaData().getURL());
            }
        } catch (SQLException e) {
            System.err.println("DB 연결 실패");
            System.err.println("원인: " + e.getMessage());
        }
    }
    
}