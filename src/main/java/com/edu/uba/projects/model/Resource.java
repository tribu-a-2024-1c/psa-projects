package com.edu.uba.projects.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "recurso")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "direccion")
    private String address;

    @Column(name = "telefono")
    private String phone;

    @ManyToOne
    @JoinColumn(name="proyecto_id", nullable=false)
    private Project project;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // to avoid infinite recursion when serializing the object
    private Set<Task> tasks;
}
