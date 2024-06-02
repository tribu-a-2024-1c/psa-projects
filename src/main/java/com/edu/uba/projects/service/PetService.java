package com.edu.uba.projects.service;

import com.edu.uba.projects.model.Pet;
import com.edu.uba.projects.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

	private final PetRepository petRepository;

	@Autowired
	public PetService(PetRepository petRepository) {
		this.petRepository = petRepository;
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
		return petRepository.findById(id).map(pet -> {
			pet.setName(petDetails.getName());
			pet.setType(petDetails.getType());
			return petRepository.save(pet);
		}).orElseThrow(() -> new RuntimeException("Pet not found with id " + id));
	}
}
