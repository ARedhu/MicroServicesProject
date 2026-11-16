package com.Ashish.course_service.dto;

import lombok.*;

@Data
public class CourseReqDto {
    private String title;
    private String description;
    private String instructor;
    private Double price;
}
