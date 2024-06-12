package com.edu.uba.projects.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edu.uba.projects.model.Project;
import com.edu.uba.projects.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    public List<Task> findByProject(Project project);
}
