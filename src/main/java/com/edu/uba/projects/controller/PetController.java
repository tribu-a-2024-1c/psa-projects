package com.edu.uba.projects.controller;

import com.edu.uba.projects.model.Pet;
import com.edu.uba.projects.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
public class PetController {

	private final PetService petService;

	@Autowired
	public PetController(PetService petService) {
		this.petService = petService;
	}

	@GetMapping
	@Operation(summary = "Get all pets")
	public ResponseEntity<List<Pet>> getPets() {
		return ResponseEntity.ok(petService.getAllPets());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a pet by ID")
	public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
		Optional<Pet> pet = petService.getPetById(id);
		return pet.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	@Operation(summary = "Create a new pet")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Pet created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
		return ResponseEntity.status(HttpStatus.CREATED).body(petService.createPet(pet));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a pet by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Pet deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Pet not found")
	})
	public ResponseEntity<Void> deletePet(@PathVariable Long id) {
		try {
			petService.deletePet(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a pet by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pet updated successfully"),
			@ApiResponse(responseCode = "404", description = "Pet not found")
	})
	public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet petDetails) {
		try {
			Pet updatedPet = petService.updatePet(id, petDetails);
			return ResponseEntity.ok(updatedPet);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping("/{id}/adopt")
	@Operation(summary = "Adopt a pet")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pet adopted successfully"),
			@ApiResponse(responseCode = "404", description = "Pet not found")
	})
	public ResponseEntity<Pet> adoptPet(@PathVariable Long id, @RequestParam String ownerName) {
		try {
			Pet adoptedPet = petService.adoptPet(id, ownerName);
			return ResponseEntity.ok(adoptedPet);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
