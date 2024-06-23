package com.edu.uba.projects.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String title;

    @OneToMany(mappedBy = "ticket") // cascade = CascadeType.ALL, orphanRemoval = true ?
    @JsonBackReference // to avoid infinite recursion when serializing the object
    private Set<Task> tasks;

}