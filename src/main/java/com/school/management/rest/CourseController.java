package com.school.management.rest;

import com.school.management.model.Course;
import com.school.management.model.dto.CourseDTO;
import com.school.management.model.dto.StudentDTO;
import com.school.management.service.CourseService;
import com.school.management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    private final StudentService studentService;

    /**
     * <p>
     * HTTP method: GET
     *
     * @param withoutStudents = true --> return the list of courses without any student (default: false).
     * @return the list of courses.
     */
    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> getCourses(@RequestParam(name = "without-students") Optional<Boolean> withoutStudents) {
    	return this.courseService.findCourses(withoutStudents.orElse(false));
    }

    /**
     *
     * <p>
     * HTTP method: GET
     *
     * @param id = the course id.
     * @return course info related to the id.
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.courseService.findById(id));
    }

    /**
     * <p>
     * HTTP method: GET
     *
     * @param id = the course id.
     * @return list of students enrolled in the course.
     */
    @GetMapping(value = "/{id}/students")
    public List<StudentDTO> getStudentsFromCourse(@PathVariable Long id) {
        return studentService.getStudentsFromCourse(id);
    }

    /**
     * @return list of relationships between students and courses, ordered by course and student.
     */
    @GetMapping(value = "/students")
    public ResponseEntity<?> getRelations() {
        return ResponseEntity.ok(courseService.getStudentAndCourseRelations());
    }

    /**
     * PUT methods (updating info)
     */

    /**
     * HTTP method: PUT
     *
     * @param id = the course id.
     *           //@param courseDto = JSON containing the course's name to be updated.
     *           Ex: {"name":"Calculus"}
     * @return the course's info updated.
     */
    @PutMapping(value = "/{id}")
    public CourseDTO updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDto) {
        return courseService.updateCourse(id, courseDto);
    }

    /**
     * REFACTOR
     * <p>
     * HTTP method: PUT
     *
     * @param id         = the course id.
     * @param studentIds = the ids of the students to be enrolled in the course. Limited to 50 students
     *                   Ex: [1, 2, 3]
     * @return a list containing the course id and the enrolled students.
     */
    @PutMapping(value = "/{id}/students")
    public void updateCourseStudents(@PathVariable Long id, @RequestBody List<Long> studentIds) {
        courseService.enrollStudents(id, studentIds);
    }

    /**
     * POST methods (inserting info)
     */

    /**
     *
     * <p>
     * HTTP method: POST
     * <p>
     * //@param courseDtoList = a list of courses, in JSON format, to be created.
     * Ex: [{"name": "Algebra"}, {"name": "Calculus"}]
     *
     * @return a list of the courses that were created with the submitted request.
     */
    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourses(@RequestBody List<CourseDTO> coursesDTO) {
        this.courseService.createCoursesList(coursesDTO);
    }

    /**
     * DELETE methods (removing info)
     */

    /**
     * <p>
     * HTTP method: DELETE
     *
     * @param confirmDeletion = true --> deletes all the courses, and student-courses relations.
     *                        The student table will not be modified.  (default: false)
     */
    @DeleteMapping
    public void deleteCourses(@RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        courseService.deleteCourses(confirmDeletion.orElse(false));
    }

    /**
     *
     * <p>
     * HTTP method: DELETE
     *
     * @param id = the course id.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(@PathVariable Long id, @RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        this.courseService.deleteById(id, confirmDeletion.orElse(false));
    }
}
