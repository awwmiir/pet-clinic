package bdd.PetServiceTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetService;

import static org.junit.Assert.assertEquals;

public class PetServiceStepDefs {
	@Autowired
	private PetService petService;
	@Autowired
	private OwnerRepository ownerRepository;

	private Owner owner;
	private Owner foundOwner;

	@Given("an owner with id = {} is created")
	public void createOwner(Integer id){
		owner = new Owner();
		owner.setId(id);
	}

	@Given("owners first name = {} and lastname = {}")
	public void setOwnerName(String firstName, String lastName){
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
	}

	@Given("owner lives in city = {} and at address = {}")
	public void setOwnerAddressAndCity(String city, String address){
		owner.setCity(city);
		owner.setAddress(address);
	}

	@Given("owners phone = {}")
	public void setOwnerPhone(String phone){
		owner.setTelephone(phone);
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
