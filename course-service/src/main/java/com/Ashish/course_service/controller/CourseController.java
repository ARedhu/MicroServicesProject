package com.Ashish.course_service.controller;

import com.Ashish.course_service.dto.CourseDto;
import com.Ashish.course_service.dto.CourseReqDto;
import com.Ashish.course_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseReqDto courseReqDto) {
        CourseDto createdCourse = courseService.createCourse(courseReqDto);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) throws InterruptedException {
//        Thread.sleep(70000);
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @DeleteMapping("/{id}")
    public boolean deleteCourse(@PathVariable Long id) {
        return courseService.deleteCourseById(id);
    }

    @GetMapping("/hello")
    public String hello(@RequestHeader("X-User-Id") Long userid){
        return "Hello from userId : " + userid;
    }
}
