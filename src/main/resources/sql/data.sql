USE LXP;

-- 1. 데이터 초기화 (기존 데이터 삭제 후 재삽입 시 사용)
-- DELETE FROM Contents;
-- DELETE FROM CourseSections;
-- DELETE FROM Courses;

-- 2. Courses (강의) 데이터
INSERT INTO Courses (title, description, status, level, published_at)
VALUES ('자바 입문 마스터', '프로그래밍의 기초 자바를 완벽하게 정복합니다.', 'PUBLISHED', 'BEGINNER', NOW()),
       ('실전 스프링 부트 가이드', '현업에서 바로 쓰는 스프링 부트 핵심 기술.', 'DRAFT', 'INTERMEDIATE', NULL),
       ('MySQL 데이터베이스 설계', '효율적인 DB 설계를 위한 필수 지식.', 'PUBLISHED', 'ADVANCED', '2024-04-20 10:00:00');

-- 3. CourseSections (강의 섹션) 데이터
-- 1번 강의(자바 입문)에 섹션 2개, 2번 강의(스프링 부트)에 섹션 1개 추가
INSERT INTO CourseSections (course_id, title)
VALUES (1, '자바 설치 및 환경 설정'),
       (1, '변수와 자료형'),
       (2, '스프링 부트 프로젝트 생성');

-- 4. Contents (컨텐츠) 데이터
-- 1번 섹션에 비디오와 문서, 2번 섹션에 비디오, 3번 섹션에 문서 추가
-- 상태: NORMAL(노출), HIDDEN(비노출)
INSERT INTO Contents (section_id, title, content_type, status)
VALUES (1, 'JDK 설치하기 (Window/Mac)', 'VIDEO', 'NORMAL'),
       (1, '환경 변수 설정 가이드', 'DOCUMENT', 'NORMAL'),
       (2, '자바의 변수와 상수 이해하기', 'VIDEO', 'NORMAL'),
       (3, 'application.properties 설정법', 'DOCUMENT', 'NORMAL'),
       (3, '테스트용 비공개 컨텐츠', 'VIDEO', 'HIDDEN');