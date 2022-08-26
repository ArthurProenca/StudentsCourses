package com.school.management.service;

import com.school.management.model.Course;
import com.school.management.model.dto.CourseDTO;
import com.school.management.model.dto.StudentCourseDTO;
import com.school.management.model.dto.StudentDTO;
import com.school.management.repository.CourseRepository;
import com.school.management.repository.StudentCourseRepository;
import com.school.management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final StudentCourseRepository studentCourseRepository;

    private final StudentRepository studentRepository;

    public List<CourseDTO> createCoursesList(List<CourseDTO> coursesDTO) {
        Timestamp ts = Timestamp.from(Instant.now());
        List<Course> courses = courseRepository.saveAll(coursesDTO.stream().filter(c -> c.getName() != null && !c.getName().isBlank()).map(courseDTO -> new Course(courseDTO.getName(), ts, ts)).collect(Collectors.toList()));
        return courses.stream().map(student -> new CourseDTO(student.getName(), student.getCreatedAt(), student.getUpdatedAt())).collect(Collectors.toList());
    }

    public CourseDTO findById(Long id) {
        return courseRepository.findById(id).map(CourseDTO::new).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Course not found."));
    }

    public void deleteById(Long id, Boolean confirmDelete) {
        if (!confirmDelete) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "To delete the course and student-courses relationships, inform confirm-deletion=true as a query param.");
        }
        courseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Course not found."));

        courseRepository.deleteById(id);
    }

    public List<CourseDTO> findCourses(Boolean without_students) {
        List<Course> courses = without_students ? courseRepository.findAllCoursesWithoutStudents() : courseRepository.findAllCoursesWithStudents();
        return courses.stream().map(CourseDTO::new).collect(Collectors.toList());
    }

    public void enrollStudents(Long id, List<Long> studentIds) {
        studentIds.forEach(studentId -> this.courseRepository.enrollStudent(id, studentId));
    }

    public List<StudentCourseDTO> getStudentAndCourseRelations() {
        return studentCourseRepository.findAll().stream().map(StudentCourseDTO::new).collect(Collectors.toList());

    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        Course updatedCourse = courseRepository.save(course.update(courseDTO, course));
        return new CourseDTO(updatedCourse.getId(), updatedCourse.getName(), updatedCourse.getCreatedAt(), updatedCourse.getUpdatedAt());
    }

    public void deleteCourses(Boolean confirmDelete) {
        if (!confirmDelete) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "To delete ALL courses and students-courses relationships, inform confirm-deletion=true as a query param.");
        }
        courseRepository.deleteAllCoursesAndRelations();

    }

    public List<StudentDTO> getStudentsFromCourse(Long id) {
        courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        return studentRepository.getStudentsFromCourse(id).stream().map(StudentDTO::new).collect(Collectors.toList());
    }
}
