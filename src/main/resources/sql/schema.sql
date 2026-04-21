-- 1. 데이터베이스 생성 및 선택
CREATE DATABASE IF NOT EXISTS LXP;
USE LXP;

-- 2. 기존 테이블 삭제 (초기화 시, 자식 테이블부터 삭제됨)
-- DROP TABLE IF EXISTS Contents;
-- DROP TABLE IF EXISTS CourseSections;
-- DROP TABLE IF EXISTS Courses;

-- 3. Courses (강의) 테이블
CREATE TABLE Courses
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(50)                                   NOT NULL,  -- 2자 이상 50자 이하
    description  VARCHAR(200)                                  NOT NULL,  -- 2자 이상 200자 이하
    status       ENUM ('DRAFT', 'PUBLISHED', 'ARCHIVED') DEFAULT 'DRAFT', -- 최초 생성 시 DRAFT
    level        ENUM ('BEGINNER', 'INTERMEDIATE', 'ADVANCED') NOT NULL,
    published_at DATETIME                                      NULL,      -- 발행 시 시각 기록
    created_at   DATETIME                                DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 4. CourseSections (강의 섹션) 테이블
CREATE TABLE CourseSections
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id  BIGINT      NOT NULL,
    title      VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_section_course FOREIGN KEY (course_id) REFERENCES Courses (id) ON DELETE CASCADE
);

-- 5. Contents (컨텐츠) 테이블
CREATE TABLE Contents
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    section_id   BIGINT                     NOT NULL,
    title        VARCHAR(50)                NOT NULL,
    content_type ENUM ('VIDEO', 'DOCUMENT') NOT NULL,
    status       ENUM ('NORMAL', 'HIDDEN') DEFAULT 'NORMAL',
    created_at   DATETIME                  DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME                  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_content_section FOREIGN KEY (section_id) REFERENCES CourseSections (id) ON DELETE CASCADE
);