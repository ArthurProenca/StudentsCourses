package com.school.management.rest;

import com.school.management.model.dto.CourseDTO;
import com.school.management.model.dto.StudentCourseDTO;
import com.school.management.model.dto.StudentDTO;
import com.school.management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDTO> getStudents(@RequestParam(name = "without-courses") Optional<Boolean> withoutCourses) {
        return studentService.getStudents(withoutCourses.orElse(false));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDTO getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDTO updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDto) {
        studentDto.setId(id);
        return studentService.updateStudent(studentDto);
    }

    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public List<StudentDTO> createStudents(@RequestBody List<StudentDTO> studentDTOList) {
        return studentService.createStudents(studentDTOList);
    }

    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudents(@RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        studentService.deleteAllStudents(confirmDeletion.orElse(false));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id, @RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        studentService.deleteStudent(id, confirmDeletion.orElse(false));
    }

    @GetMapping(value = "/{id}/courses")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> getCoursesFromStudent(@PathVariable Long id) {
        return studentService.findCoursesFromStudent(id);
    }


    @GetMapping(value = "/courses")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentCourseDTO> getRelations() {
        return studentService.getStudentAndCourseRelations();
    }


    @PutMapping(value = "/{id}/courses")
    @ResponseStatus(HttpStatus.OK)
    public void updateStudentCourses(@PathVariable Long id, @RequestBody List<Long> courseIds) {
        studentService.enrollStudentInCourses(id, courseIds);
    }
}
