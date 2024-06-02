package com.edu.uba.projects.service;

import com.edu.uba.projects.dto.OwnerDto;
import com.edu.uba.projects.dto.PetResponseDto;
import com.edu.uba.projects.model.Pet;
import com.edu.uba.projects.repository.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class PetService {

	private static final Logger log = LoggerFactory.getLogger(PetService.class);

	private final PetRepository petRepository;
	private final RestTemplate restTemplate;
	private final String ownersServiceUrl;

	@Autowired
	public PetService(PetRepository petRepository, RestTemplate restTemplate, @Value("${support.api.url}") String ownersServiceUrl) {
		this.petRepository = petRepository;
		this.restTemplate = restTemplate;
		this.ownersServiceUrl = ownersServiceUrl;
	}

	public List<Pet> getAllPets() {
		return petRepository.findAll();
	}

	public Optional<Pet> getPetById(Long id) {
		return petRepository.findById(id);
	}

	public Pet createPet(Pet pet) {
		return petRepository.save(pet);
	}

	public void deletePet(Long id) {
		petRepository.deleteById(id);
	}

	public Pet updatePet(Long id, Pet petDetails) {
		return petRepository.findById(id)
				.map(pet -> {
					pet.setName(petDetails.getName());
					pet.setType(petDetails.getType());
					return petRepository.save(pet);
				})
				.orElseThrow(() -> new RuntimeException("Pet not found with id " + id));
	}

	public Pet adoptPet(Long petId, String ownerName) {
		// Ensure the pet exists
		Supplier<RuntimeException> petNotFound = () -> new RuntimeException("Pet not found with id " + petId);
		Pet pet = petRepository.findById(petId).orElseThrow(petNotFound);

		// Register the owner
		OwnerDto owner = new OwnerDto(null, ownerName, petId);
		OwnerDto registeredOwner = restTemplate.postForObject(ownersServiceUrl + "/owners", owner, OwnerDto.class);
		log.info("Registered owner: {}", registeredOwner);

		return pet;
	}
	public PetResponseDto talkWithPetApi(Long petId, String message) {
		Supplier<RuntimeException> petNotFound = () -> new RuntimeException("Pet not found with id " + petId);
		Pet pet = petRepository.findById(petId).orElseThrow(petNotFound);
		log.info("Owner says: {} to pet: {}", message, pet.getName());
		return new PetResponseDto(pet.getName(), "wush wush");
	}
}
