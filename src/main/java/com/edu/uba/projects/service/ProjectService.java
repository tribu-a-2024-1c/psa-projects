package com.edu.uba.projects.service;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import com.edu.uba.projects.dto.CreateProjectDto;
import com.edu.uba.projects.model.Client;
import com.edu.uba.projects.model.Product;
import com.edu.uba.projects.model.Project;
import com.edu.uba.projects.model.Task;
import com.edu.uba.projects.repository.ClientRepository;
import com.edu.uba.projects.repository.ProductRepository;
import com.edu.uba.projects.repository.ProjectRepository;
import com.edu.uba.projects.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private  final ClientRepository clientRepository;
    private  final ProductRepository productRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ClientRepository clientRepository, ProductRepository productRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.taskRepository = taskRepository;
    }


    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(CreateProjectDto createProjectDto) {
        Project project = new Project();

        project.setTitle(createProjectDto.getTitle());
        project.setStartDate(createProjectDto.getStartDate());
        project.setEndDate(createProjectDto.getEndDate());
        project.setStatus(createProjectDto.getStatus());
        project.setDescription(createProjectDto.getDescription());

        // Buscar y asignar la entidad Client a partir del ID proporcionado en el DTO
        Client client = clientRepository.findById(createProjectDto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + createProjectDto.getClientId()));
        project.setClient(client);

        // Buscar y asignar la entidad Product a partir del ID proporcionado en el DTO
        Product product = productRepository.findById(createProjectDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + createProjectDto.getProductId()));
        project.setProduct(product);

        return projectRepository.save(project);
    }
    
    public Optional<Project> getProject(Long projectId){
        return projectRepository.findById(projectId);
                
    }

    public List<Task> getTasks(Project project){
        return taskRepository.findByProject(project);
        
    }

}

