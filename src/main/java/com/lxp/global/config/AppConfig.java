package com.lxp.global.config;

import com.lxp.course.controller.CourseController;
import com.lxp.course.repository.CourseRepository;
import com.lxp.course.repository.JdbcCourseRepository;
import com.lxp.course.service.CourseService;
import com.lxp.global.util.DatabaseUtil;
import com.lxp.user.repository.JdbcUserRepository;
import com.lxp.user.repository.UserRepository;
import java.sql.SQLException;
import java.util.Scanner;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class AppConfig {

    private final DataSource dataSource;
    private final Scanner scanner;

    public AppConfig() throws SQLException {
        this.dataSource = createDataSource();
        this.scanner = new Scanner(System.in);
    }

    private DataSource createDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(DatabaseUtil.getEnv("DB_URL"));
        ds.setUsername(DatabaseUtil.getEnv("DB_USER"));
        ds.setPassword(DatabaseUtil.getEnv("DB_PASSWORD"));
        ds.setInitialSize(2);
        ds.setMaxTotal(10);
        return ds;
    }

    public CourseController courseController() {
        return new CourseController(courseService(), scanner);
    }

    private CourseService courseService() {
        return new CourseService(courseRepository(), userRepository());
    }

    private CourseRepository courseRepository() {
        return new JdbcCourseRepository(dataSource);
    }

    private UserRepository userRepository() {
        return new JdbcUserRepository(dataSource);
    }

    public Scanner scanner() {
        return scanner;
    }
}