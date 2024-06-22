package com.edu.uba.projects.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.uba.projects.dto.CreateProjectDto;
import com.edu.uba.projects.dto.CreateTaskDto;
import com.edu.uba.projects.model.Project;
import com.edu.uba.projects.model.Resource;
import com.edu.uba.projects.model.Task;
import com.edu.uba.projects.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController()
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Get all projects")
    public ResponseEntity<List<Project>> getProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invilid input")
    })
    public ResponseEntity<Project> createProject(@RequestBody CreateProjectDto projectDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(projectDto));

    }

    @GetMapping("/{projectId}/tasks")
    @Operation(summary = "Get all tasks of a project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks found"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<List<Task>> getTasks(@PathVariable Long projectId) {
        Optional<Project> project = projectService.getProject(projectId);
        if (project.isPresent()) {
            return ResponseEntity.ok(projectService.getTasks(project.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // POST /projects/{projectId}/tasks crear una nueva tarea en un projecto

    @PostMapping("/{projectId}/tasks")
    @Operation(summary = "Create a new task in a project")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Task> createTask(@PathVariable Long projectId, @RequestBody CreateTaskDto taskDto) {
        try {
            Task newTask = projectService.createTask(projectId, taskDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{projectId}/resources")
    @Operation(summary = "Get all resources of a project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resources found"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<List<Resource>> getResources(@PathVariable Long projectId) {
        Optional<Project> project = projectService.getProject(projectId);
        if (project.isPresent()) {
            return ResponseEntity.ok(projectService.getResources(project.get()));
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/tasks")
    @Operation(summary = "Get all tasks across all projects")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(projectService.getAllTasks());
    }

    // @GetMapping("/{projectId}/tasks/{taskId}/resource")
    // 1. asignar un recurso a una tarea que no tiene asignado un recurso
       // (#27)
       // b. si la tarea tiene asignada un recurso delegarselo a otro (quitárselo al anterior)
    // 2. crear una tarea que por defecto no tiene asignado un recurso
       // (#18? lo hizo rebe). Editar para que se pueda crear sin un recurso asignado
    // 3. verificar si al crear una tarea del punto 2 necesita estar asociado a un proyecto
    //    las tareas se crean dentro de un proyecto pero se pueden crear. OK
    // 4. obtener las tareas asociadas a un recurso
    // 5. obtener las tareas asociadas a un proyecto

}
