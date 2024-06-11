package com.edu.uba.projects.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Data
@Entity
@Table(name = "Client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "contrato")
    private String contract;

    @Column(name = "direccion")
    private String address;

    @Column(name = "telefono")
    private String phone;

    @JsonManagedReference // to avoid infinite recursion when serializing the object
    @OneToMany(mappedBy = "client")
    private List<Project> projects;
}
