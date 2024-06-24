package com.edu.uba.projects.service;

import java.util.*;
import java.util.stream.Collectors;

import com.edu.uba.projects.dto.TicketDto;
import com.edu.uba.projects.model.Resource;

import com.edu.uba.projects.model.Ticket;
import jakarta.transaction.Transactional;
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
  public Optional<Project> getProject(Long projectId) {
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
      Resource resource = resourceRepository.findById(createTaskDto.getRecurso().getLegajo())
          .orElseGet(() -> {
            Resource newResource = new Resource();
            newResource.setId(createTaskDto.getRecurso().getLegajo());
            newResource.setName(createTaskDto.getRecurso().getNombre());
            newResource.setLastName(createTaskDto.getRecurso().getApellido());
            newResource.setTasks(new HashSet<>());
            return resourceRepository.save(newResource);
          });
      resource.getTasks().add(newTask);
      newTask.setResource(resource);
    }

    // save the task
    Task savedTask = taskRepository.save(newTask);

    // add the task to the project's task set
    project.getTasks().add(savedTask);

    return savedTask;
  }


  /// get tasks by project
  public List<Task> getTasks(Project project) {
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
    // Find the resource by ID or create a new one
    Resource resource = resourceRepository.findById(resourceDto.getLegajo())
        .orElseGet(() -> {
          Resource newResource = new Resource();
          newResource.setId(resourceDto.getLegajo());
          newResource.setName(resourceDto.getNombre());
          newResource.setLastName(resourceDto.getApellido());
          newResource.setTasks(new HashSet<>());
          return resourceRepository.save(newResource);
        });

    // Remove the task from the old resource if it exists
    if (task.getResource() != null) {
      task.getResource().removeTask(task);
    }

    // Add the task to the new resource
    resource.addTask(task);

    // Save the updated task and resource
    task.setResource(resource);
    taskRepository.save(task);
    resourceRepository.save(resource);

    return resource;
  }


  public Task updateTask(Task existingTask, CreateTaskDto taskDto) {
    existingTask.setTitle(taskDto.getTitle());
    existingTask.setDescription(taskDto.getDescription());
    existingTask.setStartDate(taskDto.getStartDate());
    existingTask.setEndDate(taskDto.getEndDate());
    existingTask.setStatus(taskDto.getStatus());
    existingTask.setEstimation(taskDto.getEstimation());

    // Update the resource if provided
    if (taskDto.getRecurso() != null) {
      Resource resource = resourceRepository.findById(taskDto.getRecurso().getLegajo())
          .orElseGet(() -> {
            Resource newResource = new Resource();
            newResource.setId(taskDto.getRecurso().getLegajo());
            newResource.setName(taskDto.getRecurso().getNombre());
            newResource.setLastName(taskDto.getRecurso().getApellido());
            newResource.setTasks(new HashSet<>());
            return resourceRepository.save(newResource);
          });

      // Remove task from old resource if exists
      if (existingTask.getResource() != null) {
        existingTask.getResource().removeTask(existingTask);
      }

      // Add task to new resource
      resource.addTask(existingTask);
      existingTask.setResource(resource);
    } else {
      // Remove task from old resource if exists and new resource is not provided
      if (existingTask.getResource() != null) {
        existingTask.getResource().removeTask(existingTask);
        existingTask.setResource(null);
      }
    }

    // Save the updated task
    return taskRepository.save(existingTask);
  }


  @Transactional
  public Task assignTicketToTask(Long taskId, TicketDto ticketDto) {
    Optional<Task> optionalTask = taskRepository.findById(taskId);
    if (optionalTask.isEmpty()) {
      throw new IllegalStateException("The task does not exist");
    }
    Task task = optionalTask.get();
    Ticket ticket = convertToEntity(ticketDto);
    task.setTicket(ticket);
    return taskRepository.save(task);
  }

  public Task getTaskById(Long taskId) {
    Optional<Task> optionalTask = taskRepository.findById(taskId);
    if (optionalTask.isEmpty()) {
      throw new IllegalStateException("The task does not exist");
    }
    return optionalTask.get();
  }

  private Ticket convertToEntity(TicketDto ticketDto) {
    Ticket ticket = new Ticket();
    ticket.setId(ticketDto.getId());
    ticket.setTitle(ticketDto.getTitle());
    return ticket;
  }

  private TicketDto convertToDto(Ticket ticket) {
    TicketDto ticketDto = new TicketDto();
    ticketDto.setId(ticket.getId());
    ticketDto.setTitle(ticket.getTitle());
    return ticketDto;
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
