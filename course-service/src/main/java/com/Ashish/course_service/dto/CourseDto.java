package com.Ashish.course_service.dto;

import lombok.Data;

@Data
public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private String instructor;
    private Double price;
}
