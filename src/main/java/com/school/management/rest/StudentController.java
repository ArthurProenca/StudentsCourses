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
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    private final CourseService courseService;

    /**
     * HTTP method: GET
     * <p>
     * //@param withoutCourses = true --> return the list of students without any course (default: false).
     *
     * @return the list of students.
     */

   
    @GetMapping
    public List<StudentDTO> getStudents(@RequestParam(name = "without-courses") Optional<Boolean> withoutCourses) {
        return studentService.getStudents(withoutCourses.orElse(false));
    }

    /**
     * HTTP method: GET
     *
     * @param id = the student id.
     * @return student info related to the id.
     */
    @GetMapping(value = "/{id}")
    public StudentDTO getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    /**
     * HTTP method: PUT
     *
     * @param id         = the student id.
     * @param studentDto = JSON containing the student's name and address to be updated.
     *                   Ex: {"name":"John Doe", "address": "Some address"}
     * @return the student's info updated.
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDTO updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDto) {
        studentDto.setId(id);
        return studentService.updateStudent(studentDto);
    }

    /**
     * HTTP method: POST
     *
     * @param studentDTOList = a list of students, in JSON format, to be registered. Limited to 50 students per request.
     *                       Ex: [{"name": "John Doe", "address": "Some address"},
     *                       {"name": "Jane Doe", "address": "Another address"}]
     * @return a list of the students that were registered with the submitted request.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<StudentDTO> createStudents(@RequestBody List<StudentDTO> studentDTOList) {
        return studentService.createStudents(studentDTOList);
    }

    /**
     * HTTP method: DELETE
     *
     * @param confirmDeletion = true --> deletes all the students, and student-courses relationships.
     *                        The course table will not be modified.  (default: false)
     */
    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudents(@RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        studentService.deleteAllStudents(confirmDeletion.orElse(false));
    }

    /**
     * HTTP method: DELETE
     *
     * @param confirmDeletion = true --> deletes the student, and student-courses relationships.
     *                        The course table will not be modified.  (default: false)
     * @param id              = the student id.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id, @RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        studentService.deleteStudent(id, confirmDeletion.orElse(false));
    }

    /**
     * HTTP method: GET
     * <p>
     *
     * @param id = the student id.
     * @return list of courses the student is enrolled.
     */
    @GetMapping(value = "/{id}/courses")
    public List<?> getCoursesFromStudent(@PathVariable Long id) {
        return studentService.getCoursesFromStudent(id);
    }

    /**
     * HTTP method: GET
     * <p>
     *
     * @return list of relationships between students and courses, ordered by student and course.
     */
    @GetMapping(value = "/courses")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> getRelations() {
        return ResponseEntity.ok(courseService.getStudentAndCourseRelations());
    }

    /**
     * HTTP method: PUT
     * <p>
     *
     * @param id        = the student id.
     * @param courseIds = the ids of the courses to enroll the student. Limited to 5 courses.
     *                  Ex: [1, 2, 3]
     * @return a list containing the student id and the enrolled courses.
     */
    @PutMapping(value = "/{id}/courses")
    public void updateStudentCourses(@PathVariable Long id, @RequestBody List<Long> courseIds) {
        studentService.enrollStudentInCourses(id, courseIds);
    }
}
