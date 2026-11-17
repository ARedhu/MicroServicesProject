package com.Ashish.student_service.controller;

import com.Ashish.student_service.dto.CourseDto;
import com.Ashish.student_service.dto.StudentDto;
import com.Ashish.student_service.dto.StudentReqDto;
import com.Ashish.student_service.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentControllers {

    private final StudentService studentService;

    @PostMapping
    private ResponseEntity<StudentDto> createStudent(@RequestBody StudentReqDto studentReqDto){
        StudentDto studentDto = studentService.createANewStudent(studentReqDto);
        return new ResponseEntity<>(studentDto, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    private ResponseEntity<StudentDto> getStudent(@RequestParam(required = false) Long id, @RequestParam(required = false) String email){
        StudentDto studentDto = studentService.getStudent(id, email);
        return ResponseEntity.ok(studentDto);
    }


    @GetMapping
    private ResponseEntity<List<StudentDto>> getAllStudents(){
        List<StudentDto> ls = studentService.getAllStudents();
        return ResponseEntity.ok(ls);
    }

    @DeleteMapping("/{id}")
    private boolean deleteStudent(@PathVariable Long id){
        return studentService.deleteStudent(id);
    }

    @GetMapping("/getCourses")
    private ResponseEntity<List<CourseDto>> getAllCourses(){
        return ResponseEntity.ok(studentService.getAllCourses());
    }

}
