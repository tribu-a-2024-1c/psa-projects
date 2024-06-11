package com.edu.uba.projects.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ClientProject")
public class ClientProject {

    @EmbeddedId
    private ClientProjectId id;

    @ManyToOne
    @MapsId("clientId")
    @JoinColumn(name = "cliente_id")
    private Client client;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "proyecto_id")
    private Project project;
}

@Embeddable
class ClientProjectId implements Serializable {
    @Column(name = "cliente_id")
    private UUID clientId;

    @Column(name = "proyecto_id")
    private UUID projectId;

    // hashCode, equals
}
