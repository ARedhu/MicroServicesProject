package com.Ashish.student_service.service;

import com.Ashish.student_service.dto.StudentDto;
import com.Ashish.student_service.dto.StudentReqDto;
import com.Ashish.student_service.entity.StudentEntity;
import com.Ashish.student_service.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentDto createANewStudent(StudentReqDto studentReqDto) {
        StudentEntity studentEntity = studentRepository.findByEmail(studentReqDto.getEmail());
        if (studentEntity != null) {
            log.info("Student with the email: {} already exist", studentReqDto.getEmail());
            throw new RuntimeException("Student with the email: " + studentReqDto.getEmail() + " already exists");
        }

        // 1. Map Request DTO -> Entity before saving
        StudentEntity newStudent = modelMapper.map(studentReqDto, StudentEntity.class);
        StudentEntity savedStudent = studentRepository.save(newStudent);

        // 2. Map saved Entity -> Response DTO
        return modelMapper.map(savedStudent, StudentDto.class);
    }

    // Find the student either by id or by email.
    public StudentDto getStudent(Long id, String email) {
        StudentEntity studentEntity;

        if (id != null) {
            studentEntity = studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        } else if (email != null && !email.isBlank()) {
            studentEntity = studentRepository.findByEmail(email);
            if (studentEntity == null) {
                throw new RuntimeException("Student not found with email: " + email);
            }
        } else {
            throw new IllegalArgumentException("Either 'id' or 'email' must be provided");
        }

        return modelMapper.map(studentEntity, StudentDto.class);
    }

    public List<StudentDto> getAllStudents() {
        List<StudentEntity> studentEntities = studentRepository.findAll();
        return studentEntities.stream()
                .map((studentEntity)-> modelMapper.map(studentEntity, StudentDto.class))
                .toList();
    }

    public boolean deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }
}
