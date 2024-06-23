package com.edu.uba.projects.model;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "recurso")
public class Resource {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "nombre")
	private String name;

	@Column(name = "apellido")
	private String lastName;

	@OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference // to avoid infinite recursion when serializing the object
	@JsonIgnoreProperties({"resource", "project"})
	private Set<Task> tasks;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Resource resource = (Resource) o;
		return Objects.equals(id, resource.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
