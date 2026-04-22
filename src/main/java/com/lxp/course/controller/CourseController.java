package com.lxp.course.controller;

import com.lxp.course.model.Course;
import com.lxp.course.model.enums.CourseLevel;
import com.lxp.course.service.CourseService;
import java.util.Scanner;

public class CourseController {

    private final CourseService courseService;
    private final Scanner scanner;

    public CourseController(CourseService courseService, Scanner scanner) {
        this.courseService = courseService;
        this.scanner = scanner;
    }

    /**
     * 강좌 등록 흐름을 처리한다.
     */
    public void createCourse() {
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

            Course course = courseService.createCourse(instructorId, title, description, level);
            System.out.printf("%n강좌가 등록되었습니다. [ID: %d] %s%n", course.getId(), course.getTitle());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[오류] " + e.getMessage());
        }
    }
}