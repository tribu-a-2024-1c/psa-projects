package com.edu.uba.projects.model;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;




@Data
@Entity
@Table(name = "tarea")
public class Task {

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

    @Column(name = "estimacion")
    private int estimation;

    @Column(name = "estado")
    private String status;

    @Column(name = "descripcion")
    private String description;

    // @ManyToOne
    // @JoinColumn(name = "proyecto_id", nullable=false)
    // private Project project;

    @ToString.Exclude // to avoid stackoverflow error due to circular reference when printing the object
    @JsonBackReference // to avoid infinite recursion when serializing the object
    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Project project;

   }
