package com.lxp;

import com.lxp.course.controller.CourseController;
import com.lxp.course.model.Course;
import com.lxp.course.model.CourseContent;
import com.lxp.course.model.CourseSection;
import com.lxp.course.model.enums.ContentStatus;
import com.lxp.course.model.enums.ContentType;
import com.lxp.course.model.enums.CourseLevel;
import com.lxp.global.config.AppConfig;
import java.sql.SQLException;
import java.util.Scanner;

public class Application {

    private final Scanner scanner;
    private final CourseController courseController;

    public Application(Scanner scanner, CourseController courseController) {
        this.scanner = scanner;
        this.courseController = courseController;
    }

    // 프로그램 실행 흐름 담당
    public void startMenu() {
        while (true) {
            System.out.println("\n=== LXP 관리 시스템 ===");
            System.out.println("1. 강좌 등록");
            System.out.println("2. 강좌 섹션 등록");
            System.out.println("3. 강좌 컨텐츠 등록");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> handleCreateCourse();
                case "2" -> handleCreateSection();
                case "3" -> handleCreateContent();
                case "0" -> {
                    System.out.println("종료합니다.");
                    scanner.close();
                    return; // startMenu() 메서드 종료
                }
                default -> System.out.println("[오류] 올바른 메뉴를 선택해주세요.");
            }
        }
    }

    private void handleCreateCourse() {
        System.out.println("\n=== 강좌 등록 ===");
        try {
            System.out.print("강사 ID: ");
            Long instructorId = Long.parseLong(scanner.nextLine().trim());

            System.out.print("강좌명: ");
            String title = scanner.nextLine().trim();

            System.out.print("강좌 설명: ");
            String description = scanner.nextLine().trim();

            System.out.print("난이도 (BEGINNER / INTERMEDIATE / ADVANCED): ");
            CourseLevel level = CourseLevel.from(scanner.nextLine().trim());

            Course course = courseController.createCourse(instructorId, title, description, level);
            System.out.printf("%n강좌가 등록되었습니다. [ID: %d] %s%n", course.getId(), course.getTitle());

        } catch (NumberFormatException e) {
            System.out.println("[오류] ID는 숫자로 입력해주세요.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[오류] " + e.getMessage());
        }
    }

    private void handleCreateSection() {
        System.out.println("\n=== 섹션 등록 ===");
        try {
            System.out.print("강좌 ID: ");
            Long courseId = Long.parseLong(scanner.nextLine().trim());

            System.out.print("섹션명: ");
            String title = scanner.nextLine().trim();

            CourseSection section = courseController.createSection(courseId, title);
            System.out.printf("%n섹션이 등록되었습니다. [ID: %d] %s%n", section.getId(), section.getTitle());

        } catch (NumberFormatException e) {
            System.out.println("[오류] ID는 숫자로 입력해주세요.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[오류] " + e.getMessage());
        }
    }

    private void handleCreateContent() {
        System.out.println("\n=== 콘텐츠 등록 ===");

        try {
            System.out.print("섹션 ID: ");
            Long sectionId = Long.parseLong(scanner.nextLine().trim());

            System.out.print("콘텐츠명: ");
            String title = scanner.nextLine().trim();

            System.out.print("콘텐츠 타입 (VIDEO / DOCUMENT): ");
            ContentType type = ContentType.from(scanner.nextLine().trim());

            System.out.print("콘텐츠 상태 (NORMAL / HIDDEN): ");
            ContentStatus status = ContentStatus.from(scanner.nextLine().trim());

            CourseContent content = courseController.createContent(sectionId, title, type, status);
            System.out.printf("%n콘텐츠가 등록되었습니다. [ID: %d] %s%n", content.getId(), content.getTitle());

        } catch (NumberFormatException e) {
            System.out.println("[오류] ID는 숫자로 입력해주세요.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[오류] " + e.getMessage());
        }
    }

    // 프로그램 진입점 (객체 생성 및 조립만 담당)
    public static void main(String[] args) {
        try {
            AppConfig config = new AppConfig();
            Application app = new Application(config.scanner(), config.courseController());
            app.startMenu();
        } catch (SQLException e) {
            System.err.println("초기화 실패: " + e.getMessage());
        }
    }
}