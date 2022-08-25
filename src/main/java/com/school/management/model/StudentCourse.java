package com.school.management.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_student_course")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentCourse {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private Long studentId;
    private Long courseId;
}
