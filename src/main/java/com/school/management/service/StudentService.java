package com.school.management.service;


import com.school.management.model.Student;
import com.school.management.model.dto.CourseDTO;
import com.school.management.model.dto.StudentCourseDTO;
import com.school.management.model.dto.StudentDTO;
import com.school.management.repository.CourseRepository;
import com.school.management.repository.StudentCourseRepository;
import com.school.management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentCourseRepository studentCourseRepository;

    private final CourseRepository courseRepository;

    public List<StudentDTO> getStudents(Boolean withoutCourses) {
        List<Student> students = withoutCourses ? studentRepository.findAllStudentsWithoutCourses() : studentRepository.findAllStudentsWithCourses();
        return students.stream().map(StudentDTO::new).collect(Collectors.toList());
    }

    public StudentDTO getStudent(Long id) {
        return studentRepository.findById(id)
                .map(StudentDTO::new)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found."));
    }


    @Transactional
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        Student student = studentRepository.findById(studentDTO.getId()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found."));


        if(studentDTO.getName() != null) {
            student.setName(studentDTO.getName());
        }
        if(studentDTO.getAddress() != null) {
            student.setAddress(studentDTO.getAddress());
        }

        student.setUpdatedAt(Timestamp.from(Instant.now()));
        Student updated = studentRepository.save(student);
        return new StudentDTO(updated.getId(), updated.getName(), updated.getAddress(), updated.getCreatedAt(), updated.getUpdatedAt());
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
        if (!confirmDeletion) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "To delete ALL students and students-courses relationships, inform confirm-deletion=true as a query param.");
        }
        studentRepository.deleteAll();

    }

    @Transactional
    public void deleteStudent(Long id, Boolean confirmDeletion) {
        if (!confirmDeletion) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "To delete the student and student-courses relationships, inform confirm-deletion=true as a query param.");
        }
        studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found."));
        studentRepository.deleteById(id);
    }

    public List<CourseDTO> findCoursesFromStudent(Long id) {
        studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found."));
        return courseRepository.findCoursesFromStudent(id).stream().map(CourseDTO::new).collect(Collectors.toList());


    }

    @Transactional
    public void enrollStudentInCourses(Long id, List<Long> courseIds) {
        studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found."));
        courseIds.forEach(courseId -> courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Course " + courseId + " not found.")));

        if(studentCourseRepository.countCoursesFromStudent(id) + 1 > 5) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "A student can not be enrolled in more than 5 courses.");
        }

        courseIds.forEach(courseId -> studentRepository.enrollStudent(id, courseId));
    }

    public List<StudentCourseDTO> getStudentAndCourseRelations() {
        return studentCourseRepository.findAll().stream().map(StudentCourseDTO::new).collect(Collectors.toList());
    }
}
