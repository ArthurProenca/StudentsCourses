package com.school.management.model.dto;

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

    public CourseDTO (Long id){
        this.id = id;
    }

    public CourseDTO(String name, Timestamp createdAt, Timestamp updatedAt) {
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
