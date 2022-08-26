package com.school.management.model.dto;

import com.school.management.model.Student;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
public class StudentDTO {

	private Long id;

	private String name;

	private String address;

	private Timestamp createdAt;

	private Timestamp updatedAt;
	public StudentDTO(String name, String address) {
		Timestamp ts = Timestamp.from(Instant.now());
		this.id = 0L;
		this.name = name;
		this.address = address;
		this.createdAt = ts;
		this.updatedAt = ts;
	}

	public StudentDTO(Long id, String name, String address, Timestamp createdAt, Timestamp updatedAt) {
		this(name, address);
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public StudentDTO(Student student) {
		this(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt());
	}
}
