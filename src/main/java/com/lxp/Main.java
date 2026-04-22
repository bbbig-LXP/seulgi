package com.lxp;

import com.lxp.course.controller.CourseController;
import com.lxp.global.config.AppConfig;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AppConfig config;
        try {
            config = new AppConfig();
        } catch (SQLException e) {
            System.err.println("초기화 실패: " + e.getMessage());
            return;
        }

        Scanner scanner = config.scanner();
        CourseController courseController = config.courseController();

        while (true) {
            System.out.println("\n=== LXP 관리 시스템 ===");
            System.out.println("1. 강좌 등록");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> courseController.createCourse();
                case "0" -> {
                    System.out.println("종료합니다.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("[오류] 올바른 메뉴를 선택해주세요.");
            }
        }
    }
}