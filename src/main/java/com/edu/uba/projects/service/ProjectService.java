package com.edu.uba.projects.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.uba.projects.dto.AssignResourceDto;
import com.edu.uba.projects.dto.CreateProjectDto;
import com.edu.uba.projects.dto.CreateTaskDto;
import com.edu.uba.projects.dto.TicketDto;
import com.edu.uba.projects.model.Project;
import com.edu.uba.projects.model.Resource;
import com.edu.uba.projects.model.Task;
import com.edu.uba.projects.model.Ticket;
import com.edu.uba.projects.repository.ProjectRepository;
import com.edu.uba.projects.repository.ResourceRepository;
import com.edu.uba.projects.repository.TaskRepository;
import com.edu.uba.projects.repository.TicketRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {

  private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

  private final ProjectRepository projectRepository;
  private final TaskRepository taskRepository;
  private final ResourceRepository resourceRepository;
  private final TicketRepository ticketRepository;

  @Autowired
  public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, ResourceRepository resourceRepository, TicketRepository ticketRepository) {
    this.projectRepository = projectRepository;
    this.taskRepository = taskRepository;
    this.resourceRepository = resourceRepository;
    this.ticketRepository = ticketRepository;
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
    project.setLeader(null);

    Project newProject = projectRepository.save(project);

    Optional<Resource> optionalResource = resourceRepository.findById(createProjectDto.getLeader().getLegajo());

    if (optionalResource.isPresent()) {
      Resource resource = optionalResource.get();
      resource.getProjects().add(newProject);
      resourceRepository.save(resource);
      newProject.setLeader(resource);
      
    }
    else {
      Resource resource = new Resource();
      resource.setId(createProjectDto.getLeader().getLegajo());
      resource.setName(createProjectDto.getLeader().getNombre());
      resource.setLastName(createProjectDto.getLeader().getApellido());
      resource.setTasks(new HashSet<>());
      resource.setProjects(new HashSet<>());
      resource.getProjects().add(newProject);
      Resource newResource = resourceRepository.save(resource);
      newProject.setLeader(newResource);
     
    }
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
    logger.info("🔍 Fetching task with id: {}", taskId);
    Optional<Task> optionalTask = taskRepository.findById(taskId);
    if (optionalTask.isEmpty()) {
      logger.error("❌ Task with id {} does not exist", taskId);
      throw new IllegalStateException("The task does not exist");
    }
    Task task = optionalTask.get();
    logger.info("✅ Task found: {}", task);

    logger.info("🔍 Fetching ticket with id: {}", ticketDto.getId());
    Ticket ticket = ticketRepository.findById(ticketDto.getId())
        .orElseGet(() -> {
          logger.info("📦 Creating new ticket with id: {}", ticketDto.getId());
          Ticket newTicket = new Ticket();
          newTicket.setId(ticketDto.getId());
          newTicket.setTitle(ticketDto.getTitle());
          return ticketRepository.save(newTicket);
        });

    logger.info("🔗 Assigning ticket to task");
    task.setTicket(ticket);
    Task updatedTask = taskRepository.save(task);
    logger.info("✅ Task updated: {}", updatedTask);

    return updatedTask;
  }

  public Task getTaskById(Long taskId) {
    logger.info("🔍 Fetching task by id: {}", taskId);
    Optional<Task> optionalTask = taskRepository.findById(taskId);
    if (optionalTask.isEmpty()) {
      logger.error("❌ Task with id {} does not exist", taskId);
      throw new IllegalStateException("The task does not exist");
    }
    Task task = optionalTask.get();
    logger.info("✅ Task found: {}", task);
    return task;
  }

  public Project finalizeProject(Long projectId) {
    logger.info("🔍 Fetching project with id: {}", projectId);
    Optional<Project> optionalProject = projectRepository.findById(projectId);
    if (optionalProject.isEmpty()) {
      logger.error("❌ Project with id {} does not exist", projectId);
      throw new IllegalStateException("The project does not exist");
    }
    Project project = optionalProject.get();
    logger.info("✅ Project found: {}", project);

    logger.info("🔍 Fetching tasks for project");
    List<Task> tasks = taskRepository.findByProject(project);
    logger.info("✅ Tasks found: {}", tasks);

    if (tasks.stream().anyMatch(task -> task.getStatus() == "Finalizado")) {
      logger.error("❌ Project cannot be finalized because there are tasks that are not finalized");
      throw new IllegalStateException("The project cannot be finalized because there are tasks that are not finalized");
    }

    logger.info("🔍 Finalizing project");
    project.setStatus("Finalizado");
    Project finalizedProject = projectRepository.save(project);
    logger.info("✅ Project finalized: {}", finalizedProject);

    return finalizedProject;
  }
}
