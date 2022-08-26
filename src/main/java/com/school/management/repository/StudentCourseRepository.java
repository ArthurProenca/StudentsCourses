package com.school.management.repository;

import com.school.management.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    @Query(value = "SELECT DISTINCT count(*) FROM t_student_course where course_id = ?1", nativeQuery = true)
    Integer countStudentCourseByCourse(Long id);

    @Query(value = "SELECT DISTINCT count(*) FROM t_student_course where student_id = ?1", nativeQuery = true)
    Integer countCoursesFromStudent(Long id);
//    Integer countStudentCourseByStudent();
}

