package com.Ashish.course_service.repository;

import com.Ashish.course_service.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    boolean existsByTitleIgnoreCase(String title);
}