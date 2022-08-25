package com.school.management.repository;

import com.school.management.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {


    @Query("select s.courseId from StudentCourse s where s.studentId = ?1")
    List<Long> findCourseIdByStudentId(Long id);
}

