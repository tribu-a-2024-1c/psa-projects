package com.edu.uba.projects.repository;

import com.edu.uba.projects.model.Project;
import com.edu.uba.projects.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {}
