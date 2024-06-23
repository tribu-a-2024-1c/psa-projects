package com.edu.uba.projects.service;

import java.util.*;
import java.util.stream.Collectors;

import com.edu.uba.projects.model.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.uba.projects.dto.AssignResourceDto;
import com.edu.uba.projects.dto.CreateProjectDto;
import com.edu.uba.projects.dto.CreateTaskDto;
import com.edu.uba.projects.model.Project;
import com.edu.uba.projects.model.Task;
import com.edu.uba.projects.repository.ProjectRepository;
import com.edu.uba.projects.repository.ResourceRepository;
import com.edu.uba.projects.repository.TaskRepository;

@Service
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final TaskRepository taskRepository;
  private final ResourceRepository resourceRepository;

  @Autowired
  public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, ResourceRepository resourceRepository) {
    this.projectRepository = projectRepository;
    this.taskRepository = taskRepository;
    this.resourceRepository = resourceRepository;
  }

  /// get all projects
  public List<Project> getAllProjects() {
    return projectRepository.findAll();
  }

  /// create a project
  public Project createProject(CreateProjectDto createProjectDto) {
    Project project = new Project();

    project.setTitle(createProjectDto.getTitle());
    project.setStartDate(createProjectDto.getStartDate());
    project.setEndDate(createProjectDto.getEndDate());
    project.setStatus(createProjectDto.getStatus());
    project.setDescription(createProjectDto.getDescription());
    project.setTasks(new HashSet<>());

    return projectRepository.save(project);
  }

  /// get project by id
  public Optional<Project> getProject(Long projectId){
    return projectRepository.findById(projectId);
  }

  /// create a task
  public Task createTask(Long projectId, CreateTaskDto createTaskDto) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

    Task newTask = new Task();
    newTask.setTitle(createTaskDto.getTitle());
    newTask.setDescription(createTaskDto.getDescription());
    newTask.setStartDate(createTaskDto.getStartDate());
    newTask.setEndDate(createTaskDto.getEndDate());
    newTask.setStatus(createTaskDto.getStatus());
    newTask.setEstimation(createTaskDto.getEstimation());
    newTask.setProject(project);
    if (createTaskDto.getRecurso() != null) {
      Resource resource = new Resource();
      resource.setId(createTaskDto.getRecurso().getLegajo());
      resource.setName(createTaskDto.getRecurso().getNombre());
      resource.setLastName(createTaskDto.getRecurso().getApellido());
      newTask.setResource(resource);
      resourceRepository.save(resource);
    }

    // save the task
    Task savedTask = taskRepository.save(newTask);

    // add the task to the project
    project.getTasks().add(savedTask);

    return savedTask;
  }

  /// get tasks by project
  public List<Task> getTasks(Project project){
    return taskRepository.findByProject(project);
  }

  /// get tasks by resource
  public List<Task> getTasks(Resource resource) {
    return taskRepository.findByResource(resource);
  }

  /// get all tasks
  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  /// get task by id
  public Optional<Task> getTask(Long taskId) {
    return taskRepository.findById(taskId);
  }


  /// get resources by project
  public List<Resource> getResources(Project project) {
    Set<Task> tasks = project.getTasks();
    return tasks.stream()
        .map(Task::getResource)
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.toList());
  }


  /// get resource by id
  public Optional<Resource> getResource(Long resourceId) {
    return resourceRepository.findById(resourceId);
  }

  /// assign a resource to a task
  public Resource assignResourceToTask(Task task, AssignResourceDto resourceDto) {
    Resource resource = new Resource();
    resource.setName(resourceDto.getName());
    resource.setTasks(new HashSet<>());
    // set tasks
    if (task.getResource() != null) {
      /// remove the task from the previous resource
      task.getResource().getTasks().remove(task);
    }

    resource.getTasks().add(task);
    Resource newResource = resourceRepository.save(resource);
    try {
    task.setResource(newResource);
    taskRepository.save(task);

    } catch (Exception e) {
      System.out.println("Error saving task: " + e.getMessage());
      throw new RuntimeException("Error saving task: " + e.getMessage());
    }
    return newResource;
  }
  public Task updateTask(Task existingTask, CreateTaskDto taskDto) {
    existingTask.setTitle(taskDto.getTitle());
    existingTask.setDescription(taskDto.getDescription());
    existingTask.setStartDate(taskDto.getStartDate());
    existingTask.setEndDate(taskDto.getEndDate());
    existingTask.setStatus(taskDto.getStatus());
    existingTask.setEstimation(taskDto.getEstimation());

    // Set the resource
    if (taskDto.getRecurso() != null) {
      Resource resource = new Resource();
      resource.setId(taskDto.getRecurso().getLegajo());
      resource.setName(taskDto.getRecurso().getNombre());
      resource.setLastName(taskDto.getRecurso().getApellido());
      existingTask.setResource(resource);
      resourceRepository.save(resource);
    }

    // Save the updated task
    return taskRepository.save(existingTask);
  }



//   public Task assignResourceToTask(Task task, AssignResourceDto resourceDto) {
//     Resource resource = new Resource();
//     try {
//         resource.setName(resourceDto.getName());
//         resource.setAddress(resourceDto.getAddress());
//         resource.setPhone(resourceDto.getPhone());
//         resource.setProject(task.getProject());
//         resource.setTasks(new HashSet<>());
//     } catch (Exception e) {
//         System.out.println("Error setting resource properties: " + e.getMessage());
//     }

//     try {
//         if (task.getResource() != null) {
//             task.getResource().getTasks().remove(task);
//         }
//     } catch (Exception e) {
//         System.out.println("Error removing task from previous resource: " + e.getMessage());
//     }

//     try {
//         resource.getTasks().add(task);
//     } catch (Exception e) {
//         System.out.println("Error adding task to new resource: " + e.getMessage());
//     }

//     try {
//         resourceRepository.save(resource);
//     } catch (Exception e) {
//         System.out.println("Error saving new resource: " + e.getMessage());
//     }

//     try {
//         task.setResource(resource);
//     } catch (Exception e) {
//         System.out.println("Error setting new resource on task: " + e.getMessage());
//     }

//     try {
//         return taskRepository.save(task);
//     } catch (Exception e) {
//         System.out.println("Error saving task: " + e.getMessage());
//         return null;
//     }
// }

}
