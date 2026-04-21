-- data.sql

USE LXP;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE Enrollments;
TRUNCATE TABLE Contents;
TRUNCATE TABLE CourseSections;
TRUNCATE TABLE Courses;
TRUNCATE TABLE Users;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO Users (name, type)
VALUES ('김강사', 'INSTRUCTOR'),
       ('이강사', 'INSTRUCTOR'),
       ('박학생', 'STUDENT'),
       ('최학생', 'STUDENT');

INSERT INTO Courses (title, description, instructor_id, status, level, published_at)
VALUES ('자바 입문 마스터', '프로그래밍의 기초 자바를 완벽하게 정복합니다.', 1, 'PUBLISHED', 'BEGINNER', NOW()),
       ('실전 스프링 부트 가이드', '현업에서 바로 쓰는 스프링 부트 핵심 기술.', 1, 'DRAFT', 'INTERMEDIATE', NULL),
       ('MySQL 데이터베이스 설계', '효율적인 DB 설계를 위한 필수 지식.', 2, 'PUBLISHED', 'ADVANCED', '2024-04-20 10:00:00');

INSERT INTO CourseSections (course_id, title)
VALUES (1, '자바 설치 및 환경 설정'),
       (1, '변수와 자료형'),
       (2, '스프링 부트 프로젝트 생성');

INSERT INTO Contents (section_id, title, content_type, status)
VALUES (1, 'JDK 설치하기 (Window/Mac)', 'VIDEO', 'NORMAL'),
       (1, '환경 변수 설정 가이드', 'DOCUMENT', 'NORMAL'),
       (2, '자바의 변수와 상수 이해하기', 'VIDEO', 'NORMAL'),
       (3, 'application.properties 설정법', 'DOCUMENT', 'NORMAL'),
       (3, '테스트용 비공개 컨텐츠', 'VIDEO', 'HIDDEN');