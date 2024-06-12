package com.edu.uba.projects.controller;

import com.edu.uba.projects.dto.CreateProjectDto;
import com.edu.uba.projects.model.Project;
import com.edu.uba.projects.model.Task;
import com.edu.uba.projects.model.Resource;
import com.edu.uba.projects.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            @ApiResponse(responseCode = "201", description = "Pet created successfully"),
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

}
