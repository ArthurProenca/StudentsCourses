package com.school.management.model.dto;

import com.school.management.model.StudentCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseDTO {
    private String course;
    private String student;

    public StudentCourseDTO(StudentCourse studentCourse) {
        this(studentCourse.getCourse().getName(), studentCourse.getStudent().getName());
    }
}
