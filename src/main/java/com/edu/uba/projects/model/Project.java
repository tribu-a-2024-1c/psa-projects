package com.edu.uba.projects.model;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "proyecto")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String title;

    @Column(name = "fecha_inicio")
    private Date startDate;

    @Column(name = "fecha_fin")
    private Date endDate;

    @Column(name = "estado")
    private String status;

    @Column(name = "descripcion")
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // to avoid infinite recursion when serializing the object
    private Set<Task> tasks;
}
