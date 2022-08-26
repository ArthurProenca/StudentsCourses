package com.school.management.model;

import com.school.management.model.dto.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;


    public Course(String name, Timestamp ts, Timestamp ts1) {
        this.name = name;
        this.createdAt = ts;
        this.updatedAt = ts1;
    }

    public Course update(CourseDTO courseDTO, Course course) {
        return new Course(course.getId(), courseDTO.getName(), course.getCreatedAt(), Timestamp.from(Instant.now()));
    }
}
