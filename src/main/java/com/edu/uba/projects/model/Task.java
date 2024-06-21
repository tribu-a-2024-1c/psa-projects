package com.edu.uba.projects.model;

import java.util.Date;

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

    @ManyToOne
    @JoinColumn(name="proyecto_id", nullable=false)
    private Project project;

    // @OneToMany
    // @Column(name = 'recurso')
    // private Resource resource;

}