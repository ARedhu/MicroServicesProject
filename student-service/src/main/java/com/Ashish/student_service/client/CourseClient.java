package com.Ashish.student_service.client;

import com.Ashish.student_service.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//This is kind of controllers.
@FeignClient(name = "course-service") // We defined the name here, so that we don't have to use the URL, and loadbalancer is working under the hood.
public interface CourseClient {

    @GetMapping("/courses/{id}") // As we want to make a get request, so get mapping and appending url here.
    CourseDto getCourse(@PathVariable Long id);
}
