package com.Ashish.student_service.repository;


import com.Ashish.student_service.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    StudentEntity findByEmail(String email);
}
