package com.edu.uba.projects.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@Table(name = "cliente")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
