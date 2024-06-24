package com.edu.uba.projects.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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


	// project leader of the resource
	@OneToMany(mappedBy = "leader", cascade = CascadeType.ALL)
	@JsonManagedReference // to avoid infinite recursion when serializing the object
	@JsonIgnoreProperties({"leader", "tasks"})
	private Set<Project> projects = new HashSet<>();



	@OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
	@JsonManagedReference // to avoid infinite recursion when serializing the object
	@JsonIgnoreProperties({"resource", "project"})
	private Set<Task> tasks = new HashSet<>();

	public void addTask(Task task) {
		tasks.add(task);
		task.setResource(this);
	}

	public void removeTask(Task task) {
		tasks.remove(task);
		task.setResource(null);
	}

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
