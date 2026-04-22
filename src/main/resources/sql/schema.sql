-- schema.sql

CREATE DATABASE IF NOT EXISTS LXP;
USE LXP;

CREATE TABLE Users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50)                             NOT NULL,
    type       ENUM ('STUDENT', 'INSTRUCTOR', 'ADMIN') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Courses
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(50)                                   NOT NULL,
    description   VARCHAR(200)                                  NOT NULL,
    instructor_id BIGINT                                        NOT NULL,
    status        ENUM ('DRAFT', 'PUBLISHED', 'ARCHIVED') DEFAULT 'DRAFT',
    level         ENUM ('BEGINNER', 'INTERMEDIATE', 'ADVANCED') NOT NULL,
    published_at  DATETIME                                      NULL,
    created_at    DATETIME                                DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_course_instructor FOREIGN KEY (instructor_id) REFERENCES Users (id)
);

CREATE TABLE CourseSections
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id  BIGINT      NOT NULL,
    title      VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_section_course FOREIGN KEY (course_id) REFERENCES Courses (id) ON DELETE CASCADE
);

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

CREATE TABLE Enrollments
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id    BIGINT   NOT NULL,
    user_id      BIGINT   NOT NULL,
    completed_at DATETIME NULL,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES Courses (id),
    CONSTRAINT fk_enrollment_user FOREIGN KEY (user_id) REFERENCES Users (id),
    CONSTRAINT uq_enrollment UNIQUE (course_id, user_id)
);