package com.school.management.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Student(String name, String address, Timestamp createdAt, Timestamp updatedAt) {
		this.name = name;
		this.address = address;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
