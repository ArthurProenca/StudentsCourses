package com.school.management.model.dto;

import com.school.management.model.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long id;
    private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public CourseDTO(String name, Timestamp createdAt, Timestamp updatedAt) {
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CourseDTO(Course course) {
        this(course.getId(), course.getName(), course.getCreatedAt(), course.getUpdatedAt());
    }

}
