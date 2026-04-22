package com.lxp.course.repository;

import com.lxp.course.model.CourseSection;
import java.util.Optional;

public interface CourseSectionRepository {

    CourseSection save(CourseSection courseSection);

    Optional<CourseSection> findById(Long id);
    
}
