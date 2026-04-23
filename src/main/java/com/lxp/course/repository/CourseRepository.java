package com.lxp.course.repository;

import com.lxp.course.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    Course save(Course course);

    Optional<Course> findById(Long id);

    List<Course> findAll();

    Optional<Course> findWithSectionsAndContentsById(Long id);

    void update(Course course);

}
