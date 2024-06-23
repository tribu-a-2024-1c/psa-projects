package com.edu.uba.projects.model;

import java.util.Date;
import java.util.Objects;

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

    @ManyToOne
    @ToString.Exclude // to avoid stackoverflow error due to circular reference when printing the object
    @JoinColumn(name="proyecto_id", nullable=false)
    private Project project;

    @ManyToOne
    @ToString.Exclude // to avoid stackoverflow error due to circular reference when printing the object
    @JoinColumn(name="recurso_id")
    private Resource resource;

    @ManyToOne
    @ToString.Exclude  // to avoid stackoverflow error due to circular reference when printing the object
    @JoinColumn(name="ticket_id")
    private Ticket ticket;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
