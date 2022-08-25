package com.school.management.repository;

import com.school.management.model.Course;
import com.school.management.model.dto.CourseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "INSERT INTO t_student_course (course_id, student_id) VALUES (?1, ?2)", nativeQuery = true)
    void enrollStudent(Long course_id, Long student_id);

    @Query(value = "SELECT * FROM course WHERE id NOT IN (SELECT course_id FROM t_student_course)", nativeQuery = true)
    List<Course> findAllCoursesWithoutStudents();

    @Query(value = "SELECT * FROM course WHERE id IN (SELECT course_id FROM t_student_course)", nativeQuery = true)
    List<Course> findAllCoursesWithStudents();

    @Query(value = "DELETE FROM course", nativeQuery = true)
    void deleteAllCoursesAndRelations();
}
