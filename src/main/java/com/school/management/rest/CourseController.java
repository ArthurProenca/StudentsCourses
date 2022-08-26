package com.school.management.rest;

import com.school.management.model.dto.CourseDTO;
import com.school.management.model.dto.StudentCourseDTO;
import com.school.management.model.dto.StudentDTO;
import com.school.management.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> getCourses(@RequestParam(name = "without-students") Optional<Boolean> withoutStudents) {
    	return this.courseService.findCourses(withoutStudents.orElse(false));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO getCourse(@PathVariable Long id) {
        return this.courseService.findById(id);
    }


    @GetMapping(value = "/{id}/students")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDTO> getStudentsFromCourse(@PathVariable Long id) {
        return courseService.getStudentsFromCourse(id);
    }

    @GetMapping(value = "/students")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentCourseDTO> getRelations() {
        return courseService.getStudentAndCourseRelations();
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDto) {
        return courseService.updateCourse(id, courseDto);
    }

    @PutMapping(value = "/{id}/students")
    @ResponseStatus(HttpStatus.OK)
    public void updateCourseStudents(@PathVariable Long id, @RequestBody List<Long> studentIds) {
        courseService.enrollStudents(id, studentIds);
    }


    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CourseDTO> createCourses(@RequestBody List<CourseDTO> coursesDTO) {
        return this.courseService.createCoursesList(coursesDTO);
    }

    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourses(@RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        courseService.deleteCourses(confirmDeletion.orElse(false));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(@PathVariable Long id, @RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        this.courseService.deleteById(id, confirmDeletion.orElse(false));
    }
}
