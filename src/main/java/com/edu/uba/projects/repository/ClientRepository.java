package com.edu.uba.projects.repository;

import com.edu.uba.projects.model.Client;
import com.edu.uba.projects.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {}
