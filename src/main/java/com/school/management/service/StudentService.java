package com.school.management.service;


import com.school.management.model.Course;
import com.school.management.model.Student;
import com.school.management.model.StudentCourse;
import com.school.management.model.dto.CourseDTO;
import com.school.management.model.dto.StudentDTO;
import com.school.management.repository.CourseRepository;
import com.school.management.repository.StudentCourseRepository;
import com.school.management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.util.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.rmi.server.LogStream.log;

@Service
@RequiredArgsConstructor
@Log4j2
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentCourseRepository studentCourseRepository;

    private final CourseRepository courseRepository;

    public List<StudentDTO> getStudents(Boolean withoutCourses) {
        return studentRepository.findAll().stream()
                .map(student -> new StudentDTO(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    public StudentDTO getStudent(Long id) {
        return studentRepository.findById(id)
                .map(student -> new StudentDTO(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found."));
    }

    @Transactional
    public StudentDTO updateStudent(StudentDTO studentDto) {
        Student student = studentRepository.findById(studentDto.getId()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found."));

        Boolean updated = false;
        if (studentDto.getName() != null && !studentDto.getName().isBlank() && !studentDto.getName().equals(student.getName())) {
            student.setName(studentDto.getName());
            updated = true;
        }
        if (studentDto.getAddress() != null && !studentDto.getAddress().isBlank() && !studentDto.getAddress().equals(student.getAddress())) {
            student.setAddress(studentDto.getAddress());
            updated = true;
        }

        if (updated) {
            student.setUpdatedAt(Timestamp.from(Instant.now()));
            student = studentRepository.save(student);
        }

        return new StudentDTO(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt());
    }

    public List<StudentDTO> createStudents(List<StudentDTO> studentsDto) {
        log.info("Creating [{}] students ", studentsDto.size());
        if (studentsDto.size() > 50) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "A request can not contain more than 50 students.");
        }

        Timestamp ts = Timestamp.from(Instant.now());
        List<Student> l = studentRepository.saveAll(studentsDto.stream()
                .filter(s -> s.getName() != null && !s.getName().isBlank() && s.getAddress() != null && !s.getAddress().isBlank())
                .map(studentDTO -> new Student(studentDTO.getName(),
                        studentDTO.getAddress(),
                        ts,
                        ts))
                .collect(Collectors.toList()));

        return l.stream()
                .map(student -> new StudentDTO(student.getId(),
                        student.getName(),
                        student.getAddress(),
                        student.getCreatedAt(),
                        student.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllStudents(Boolean confirmDeletion) {
        if (confirmDeletion) {
            studentRepository.deleteAll();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "To delete ALL students and students-courses relationships, inform confirm-deletion=true as a query param.");
        }
    }

    @Transactional
    public void deleteStudent(Long id, Boolean confirmDeletion) {
        if (confirmDeletion) {
            Student student = studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Student not found."));
            studentRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "To delete the student and student-courses relationships, inform confirm-deletion=true as a query param.");
        }


    }

    public List<StudentDTO> getStudentsFromCourse(Long id) {
        return studentRepository.findAllByCourses_Id(id).stream()
                .map(student -> new StudentDTO(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    public List<?> getCoursesFromStudent(Long id) {
        List<CourseDTO> coursesDTO = Lists.newArrayList();
        studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found."));

        studentCourseRepository.findCourseIdByStudentId(id).stream().map(
                courseId -> courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found."))
        ).forEach(course -> coursesDTO.add(new CourseDTO(course.getId(), course.getName(), course.getCreatedAt(), course.getUpdatedAt())));

        return coursesDTO;

    }

    public void enrollStudentInCourses(Long id, List<Long> courseIds) {
        courseIds.stream().forEach(courseId -> {
            studentRepository.enrollStudent(id, courseId);
        });
    }
}
