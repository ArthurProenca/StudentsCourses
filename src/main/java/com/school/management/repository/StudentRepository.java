package com.school.management.repository;

import com.school.management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "SELECT * FROM student WHERE id NOT IN (SELECT student_id FROM t_student_course)", nativeQuery = true)
    List<Student> findAllStudentsWithoutCourses();

    @Query(value = "SELECT * FROM student WHERE id IN (SELECT student_id FROM t_student_course)", nativeQuery = true)
    List<Student> findAllStudentsWithCourses();

    @Modifying
    @Query(value = "INSERT INTO t_student_course (student_id, course_id) VALUES (?1, ?2)", nativeQuery = true)
    void enrollStudent(Long id, Long courseId);

    @Query(value = "SELECT * FROM student WHERE ID IN (SELECT student_id FROM t_student_course WHERE course_id = ?1)", nativeQuery = true)
    List<Student> getStudentsFromCourse(Long id);
}
