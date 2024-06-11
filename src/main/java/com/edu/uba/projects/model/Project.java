package com.edu.uba.projects.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@Table(name = "Project")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Client client;

    @ToString.Exclude // to avoid stackoverflow error due to circular reference when printing the object
    @JsonBackReference // to avoid infinite recursion when serializing the object
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product product;
}
