package com.edu.uba.projects.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edu.uba.projects.model.Project;
import com.edu.uba.projects.model.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    public List<Resource> findByProject(Project project);
}
