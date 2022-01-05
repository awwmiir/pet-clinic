package bdd.PetServiceTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PetServiceStepDefs {
	@Autowired
	private PetService petService;
	@Autowired
	private OwnerRepository ownerRepository;
	@Autowired
	private PetRepository petRepository;
	@Autowired
	private PetTypeRepository petTypeRepository;
	@Autowired
	private PetTimedCache petTimedCache;

	private Owner owner;
	private Owner foundOwner;

	private Pet pet;
	private Pet savedPet;

	private PetType petType;

	@Given("all pets are {}")
	public void setPetType(String type){
		petType = new PetType();
		petType.setName(type);
		petTypeRepository.save(petType);
	}

	@Given("a pet with id = {} exists")
	public void thereIsAPetWithId(Integer id) {
		pet = new Pet();
		pet.setName("kakoli");
		pet.setType(petType);
		pet.setId(id);
		pet.setBirthDate(LocalDate.of(2020, 4, 7));
	}

	@Given("the owner of the pet is owner with id = {}")
	public void addPetToOwner(int ownerId) {
		createOwner(ownerId);
		owner.addPet(pet);
		saveOwnerInRepository();
		petTimedCache.save(pet);
	}

	@When("findPet with id = {} is called")
	public void findPetWithId(Integer id){
		savedPet = petService.findPet(id);
	}

	@Then("expected pet is found correctly")
	public void petIsFound(){
		assertEquals(savedPet.getName(), pet.getName());
		assertEquals(pet.getBirthDate(), savedPet.getBirthDate());
	}

	@Given("pet with id = {} exists in per repository")
	public void petExistsInPetRepository(Integer id){
		Pet pet = new Pet();
		pet.setId(id);
		petRepository.save(pet);
	}

	@When("owner requests a new pet")
	public void ownerRequestsNewPet(){
		savedPet = petService.newPet(owner);
	}

	@Then("a new pet is created and returned")
	public void newPetReturnedPetService() {
		assertNotNull(savedPet);
	}

	@Then("a new pet is added to owner pet list")
	public void newPetIsSaved() {
		assertTrue(owner.getPets().contains(savedPet));
	}

	@Given("an owner with id = {} is created")
	public void createOwner(Integer id){
		owner = new Owner();
		owner.setId(id);
		owner.setFirstName("firstName");
		owner.setLastName("lastName");
		owner.setCity("city");
		owner.setAddress("address");
		owner.setTelephone("0123456789");
	}

	@Given("owner exists in the repository")
	public void saveOwnerInRepository(){
		ownerRepository.save(owner);
	}

	@When("findOwner with Id = {} is called")
	public void findOwnerWithExistingId(Integer id) {
		foundOwner = petService.findOwner(id);
	}

	@Then("expected owner is found correctly")
	public void ownerIsFound() {
		assertEquals(owner.getId(), foundOwner.getId());
		assertEquals(owner.getAddress(), foundOwner.getAddress());
		assertEquals(owner.getCity(), foundOwner.getCity());
		assertEquals(owner.getTelephone(), foundOwner.getTelephone());
		assertEquals(owner.getFirstName(), foundOwner.getFirstName());
		assertEquals(owner.getLastName(), foundOwner.getLastName());
	}
}
