package com.Ashish.course_service.service;

import com.Ashish.course_service.dto.CourseDto;
import com.Ashish.course_service.dto.CourseReqDto;
import com.Ashish.course_service.entity.CourseEntity;
import com.Ashish.course_service.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public CourseDto createCourse(CourseReqDto courseReqDto) {
        if (courseRepository.existsByTitleIgnoreCase(courseReqDto.getTitle())) {
            log.info("Course with title '{}' already exists", courseReqDto.getTitle());
            throw new IllegalArgumentException("Course with title '" + courseReqDto.getTitle() + "' already exists");
        }

        CourseEntity courseEntity = modelMapper.map(courseReqDto, CourseEntity.class);
        CourseEntity savedCourse = courseRepository.save(courseEntity);
        return modelMapper.map(savedCourse, CourseDto.class);
    }

    public CourseDto getCourseById(Long id) {
        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return modelMapper.map(courseEntity, CourseDto.class);
    }

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .toList();
    }

    @Transactional
    public boolean deleteCourseById(Long id) {
        if (!courseRepository.existsById(id)) {
            return false;
        }
        courseRepository.deleteById(id);
        return true;
    }
}
