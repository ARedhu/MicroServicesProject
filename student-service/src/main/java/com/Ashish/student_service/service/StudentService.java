package com.Ashish.student_service.service;

import com.Ashish.student_service.client.CourseClient;
import com.Ashish.student_service.dto.CourseDto;
import com.Ashish.student_service.dto.StudentDto;
import com.Ashish.student_service.dto.StudentReqDto;
import com.Ashish.student_service.entity.StudentEntity;
import com.Ashish.student_service.repository.StudentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
//import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final RestClient restClient;
    private final CourseClient courseClient;

    public StudentService(StudentRepository studentRepository, ModelMapper modelMapper, @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder, CourseClient courseClient) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
        this.restClient = restClientBuilder.build();
        this.courseClient = courseClient;
    }

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

    /*
    // If we want to make an HTTP request to another application from our application. Or we can say if we want to make an API call from our application. And, we are using Java. We can do that by using HttpClient. Remember in Java 8 or before versions this HttpClient support was not present.

	// HttpClient can be used to send requests and retrieve their responses.
	// HttpRequest is used to create the requests.

	// Remember these are abstract classes so we can't create their object using new keyword. We can use builder.

	// In Java 9 or after versions we can use "var" keyword as well for local variables.

	Code:
	import from here: java.net.http.HttpClient
	String url = "url here";

	HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
	HttpClient client = HttpClient.newBuilder().build(); // or we can do HttpClient.newHttpClient();

	HttpResponse<String>response = client.send(request, HttpResponse.BodyHandlers.ofString()); // The second parameter tells what type of response would be or how to handle the response.
     */
    public List<CourseDto> getAllCourses() {
        // But in springboot versions 3. or later we can use RestClient.

//        String url = "http://localhost:8082/courses";
//
//        RestClient restClient = RestClient.create();

        String url = "http://course-service/courses";
        return restClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CourseDto>>() {});

    }

    @Retry(name = "courseService")
    @CircuitBreaker(name = "courseService")
    @RateLimiter(name = "courseService")
    public CourseDto getAParticularCourse(Long id) {
        return courseClient.getCourse(id);
    }
}
