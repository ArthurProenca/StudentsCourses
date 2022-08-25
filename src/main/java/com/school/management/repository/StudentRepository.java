package com.school.management.repository;

import com.school.management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "SELECT * FROM student WHERE id IN (SELECT student_id FROM t_student_course WHERE course_id = (?1))", nativeQuery = true)
    List<Student> findAllByCourses_Id(Long id);

    @Query(value = "INSERT INTO t_student_course (student_id, course_id) VALUES (?1, ?2)", nativeQuery = true)

    void enrollStudent(Long id, Long courseId);
}
