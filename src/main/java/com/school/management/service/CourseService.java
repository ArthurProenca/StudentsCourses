package com.school.management.service;

import com.school.management.model.Course;
import com.school.management.model.dto.CourseDTO;
import com.school.management.repository.CourseRepository;
import com.school.management.repository.StudentCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final StudentCourseRepository studentCourseRepository;

    public List<CourseDTO> createCoursesList(List<CourseDTO> coursesDTO){

        Timestamp ts = Timestamp.from(Instant.now());
        List<Course> courses = courseRepository.saveAll(coursesDTO.stream()
                .filter(c -> c.getName() != null && !c.getName().isBlank())
                .map(courseDTO -> new Course(courseDTO.getName(),
                        ts,
                        ts))
                .collect(Collectors.toList()));

        return courses.stream()
                .map(student -> new CourseDTO(
                        student.getName(),
                        student.getCreatedAt(),
                        student.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    public CourseDTO findById(Long id) {
        return courseRepository.findById(id).map(course -> new CourseDTO(course.getId(), course.getName(), course.getCreatedAt(), course.getUpdatedAt())).orElse(null);
    }

    public void deleteById(Long id, Boolean orElse) {
        courseRepository.deleteById(id);
    }

    public List<CourseDTO> findCourses(Boolean without_students) {
        if(without_students){
            return this.courseRepository.findAllCoursesWithoutStudents().stream().map(course -> new CourseDTO(course.getId(), course.getName(), course.getCreatedAt(), course.getUpdatedAt())).collect(Collectors.toList());
        }
        return this.courseRepository.findAllCoursesWithStudents().stream().map(course -> new CourseDTO(course.getId(), course.getName(), course.getCreatedAt(), course.getUpdatedAt())).collect(Collectors.toList());

    }

    public void enrollStudents(Long id, List<Long> studentIds) {
        studentIds.stream().forEach(studentId -> {
            this.courseRepository.enrollStudent(id, studentId);
        } );
    }

    public Object getStudentAndCourseRelations() {
        return studentCourseRepository.findAll().stream();
    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        Course updatedCourse = courseRepository.save(course.update(courseDTO, course));
        return new CourseDTO(updatedCourse.getId(), updatedCourse.getName(), updatedCourse.getCreatedAt(), updatedCourse.getUpdatedAt());
    }

    public void deleteCourses(Boolean confirmDelete) {
        if (confirmDelete) {
            courseRepository.deleteAllCoursesAndRelations();
        }
    }
}
