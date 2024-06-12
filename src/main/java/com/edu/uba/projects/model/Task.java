package com.edu.uba.projects.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;




@Data
@Entity
@Table(name = "Tarea")
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

    @ManyToOne
    @JoinColumn(name="proyecto_id", nullable=false)
    private Project project;
}