package org.springframework.samples.petclinic.owner;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.utility.SimpleDI;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PetManagerTest {
	@MockBean
	private PetTimedCache petTimedCache;
	@MockBean
	private OwnerRepository ownerRepository;
	@MockBean
	private Logger logger;
	@MockBean
	private Owner owner;

	private static final Integer OWNER_ID = 1, PET_ID = 2;
	private PetManager petManager;
	private PetType dog, cat, mouse;
	private Pet spike, tom, jerry;
	private List<Pet> pets;
	private Set<PetType> petTypes;

	@BeforeEach
	void setUp(){
		petManager = new PetManager(petTimedCache, ownerRepository, logger);
		petTypes = new HashSet<>();
		setUpTypes();
		setUpPets();
		pets = Arrays.asList(tom, spike, jerry);
	}

	void setUpTypes(){
		dog = new PetType();
		dog.setName("dog");
		petTypes.add(dog);

		cat = new PetType();
		cat.setName("cat");
		petTypes.add(cat);


		mouse = new PetType();
		mouse.setName("mouse");
		petTypes.add(mouse);
	}

	void setUpPets(){
		spike = new Pet();
		spike.setType(dog);

		tom = new Pet();
		tom.setType(cat);

		jerry = new Pet();
		jerry.setType(mouse);
	}

	@AfterEach
	void tearDown(){
		petManager = null;
		tom = null;
		spike = null;
		jerry = null;
		dog = null;
		cat = null;
		mouse = null;
	}

	/**
	 * used: ownerRepository -> mock, petTimedCache and logger -> dummy for petManager
	 * Mockisty/Classical: Mockisty
	 * State/Behavior: State Verification
	*/
	@Test
	void Method_findOwner_returns_the_correct_owner_with_the_requested_id(){
		when(ownerRepository.findById(OWNER_ID)).thenReturn(owner);
		assertEquals(petManager.findOwner(OWNER_ID), owner);
	}

	/**
	* used: TODO
	* Mockisty/Classical: Mockisty
	* State/Behavior: State Verification
	 */
	@Test
	void Method_findOwner_is_returned_if_owner_with_given_id_is_not_found(){
		Owner expectedOwner = petManager.findOwner(OWNER_ID);
		assertNull(expectedOwner);
	}

	/**
	 * used: owner and logger -> spy, petTimedCache and ownerRepository -> dummy for petManager
	 * Mockisty/Classical: TODO
	 * State/Behavior: Behavior Verification
	 */
	@Test
	void New_pet_is_created_and_is_owned_by_expected_owner(){
		Pet expectedPet = petManager.newPet(owner);
		verify(owner).addPet(expectedPet);
		verify(logger).info("add pet for owner {}", owner.getId());

	}

	/**
	 * used petTimedCache -> mock, ownerRepository and logger -> dummy for petManager
	 * Mockisty/Classical: TODO
	 * State/Behavior: State Verification
	 */
	@Test
	void Method_findPet_returns_the_correct_pet_with_given_id(){
		Pet expectedPet = new Pet();
		when(petTimedCache.get(PET_ID)).thenReturn(expectedPet);
		Pet actualPet = petManager.findPet(PET_ID);
		assertNotNull(actualPet);
		assertEquals(actualPet, expectedPet);
	}

	/**
	 * used: TODO
	 * Mockisty/Classical: TODO
	 * State/Behavior: State Verification
	 */
	@Test
	void Method_findPet_returns_Null_if_pet_with_given_id_is_not_found(){
		Pet actualPet = petManager.findPet(PET_ID);
		assertNull(actualPet);
	}

	/**
	 * used: expectedPet -> mock, logger and owner and petTimedCache -> spy, ownerRepository -> dummy for petManager
	 * Mockisty/Classical: TODO
	 * State/Behavior: Behavior Verification
	 */
	@Test
	void Pet_is_saved_in_cache_and_is_owned_by_owner(){
		Pet expectedPet = mock(Pet.class);
		when(expectedPet.getId()).thenReturn(PET_ID);
		petManager.savePet(expectedPet, owner);
		verify(logger).info("save pet {}", PET_ID);
		verify(owner).addPet(expectedPet);
		verify(petTimedCache).save(expectedPet);
	}

	/**
	 * used: owner and ownerRepository -> mock, logger and petTimedCache -> dummy for petManager
	 * Mockisty/Classical: TODO
	 * State/Behavior: State Verification
	 */
	@Test
	void Owner_pets_are_returned_correctly_using_state_verification(){
		when(owner.getPets()).thenReturn(pets);
		when(ownerRepository.findById(OWNER_ID)).thenReturn(owner);
		assertEquals(petManager.getOwnerPets(OWNER_ID), pets);
	}

	/**
	 * used: owner and ownerRepository and logger -> spy, petTimedCache -> dummy for petManager
	 * Mockisty/Classical: TODO
	 * State/Behavior: State Verification
	 */
	@Test
	void Owner_pets_are_returned_correctly_using_behavior_verification() {
		when(owner.getPets()).thenReturn(pets);
		when(ownerRepository.findById(OWNER_ID)).thenReturn(owner);
		petManager.getOwnerPets(OWNER_ID);
		verify(ownerRepository).findById(OWNER_ID);
		verify(owner).getPets();
		verify(logger).info("finding the owner's pets by id {}", OWNER_ID);
	}

	/**
	 * used: logger and petTimedCache and ownerRepository -> dummy for petManager
	 * Mockisty/Classical: TODO
	 * State/Behavior: State Verification
	 */
	@Test
	void Null_pointer_exception_is_thrown_if_owner_does_not_exist_to_get_owners_pets(){
		assertThrows(NullPointerException.class, () ->
			petManager.getOwnerPets(OWNER_ID)
		);
	}

	/**
	 * used: owner and ownerRepository -> mock, logger and petTimedCache -> dummy for petManager
	 * Mockisty/Classical: TODO
	 * State/Behavior: State Verification
	 */
	@Test
	void Owner_pet_types_are_returned_correctly(){
		when(ownerRepository.findById(OWNER_ID)).thenReturn(owner);
		when(owner.getPets()).thenReturn(pets);
		assertEquals(petManager.getOwnerPetTypes(OWNER_ID), petTypes);
	}

	/**
	 * used: logger and petTimedCache and ownerRepository -> dummy for petManager
	 * Mockisty/Classical: TODO
	 * State/Behavior: State Verification
	 */
	@Test
	void Null_pointer_exception_is_thrown_if_owner_does_not_exist_to_get_owners_pet_types(){
		assertThrows(NullPointerException.class, () ->
			petManager.getOwnerPetTypes(OWNER_ID)
		);
	}

	/**
	 * used: logger and ownerRepository -> dummy for petManager, petTimedCache and visit -> mock
	 * Mockisty/Classical: TODO
	 * State/Behavior: State Verification
	 */
	@Test
	void Method_getVisitsBetween_returns_visits_correctly(){
		Visit visit = mock(Visit.class);
		when(visit.getDate()).thenReturn(LocalDate.parse("2020-02-01"));
		tom.addVisit(visit);
		when(petTimedCache.get(PET_ID)).thenReturn(tom);
		assertThat(petManager.getVisitsBetween(PET_ID, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-03-01")))
			.isNotNull()
			.hasSize(1)
			.contains(visit);
	}

	/**
	 * used: logger and petTimedCache and ownerRepository -> dummy for petManager
	 * Mockisty/Classical: TODO
	 * State/Behavior: State Verification
	 */
	@Test
	void Method_getVisitsBetween_throws_NullPointerException_when_pet_is_not_found(){
		assertThrows(NullPointerException.class, () ->
			petManager.getVisitsBetween(PET_ID, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-03-01"))
		);
	}
}
