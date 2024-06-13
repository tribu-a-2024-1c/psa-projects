package com.edu.uba.projects.model;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

    @ToString.Exclude // to avoid stackoverflow error due to circular reference when printing the object
    @JsonBackReference // to avoid infinite recursion when serializing the object
    @ManyToOne // un proyecto tiene un solo cliente, un cliente puede tener varios proyectos
    // un proyecto NO puede tener varios clientes (ManyToMany)
    @JoinColumn(name = "cliente_id")
    private Client client;

    @ToString.Exclude // to avoid stackoverflow error due to circular reference when printing the object
    @JsonBackReference // to avoid infinite recursion when serializing the object
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product product;

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks;


}
