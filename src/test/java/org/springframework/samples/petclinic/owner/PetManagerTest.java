package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.utility.SimpleDI;

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
	private PetManager petManager;
	private PetType dog, cat, mouse;
	private Pet spike, tom, jerry;

	@BeforeEach
	void setUp(){
		petManager = new PetManager(petTimedCache, ownerRepository, logger);
		setUpTypes();
		setUpPets();
	}

	void setUpTypes(){
		dog = new PetType();
		dog.setName("dog");

		cat = new PetType();
		cat.setName("cat");

		mouse = new PetType();
		mouse.setName("mouse");
	}

	void setUpPets(){
		spike = new Pet();
		spike.setType(dog);

		tom = new Pet();
		tom.setType(cat);

		jerry = new Pet();
		jerry.setType(mouse);
	}

	@Test
	void Method_findOwner_returns_the_correct_owner_with_the_requested_id(){
		when(ownerRepository.findById(1)).thenReturn(owner);
		Owner actualOwner = petManager.findOwner(1);
		assertNotNull(actualOwner);
		assertEquals(actualOwner, owner);
		verify(ownerRepository).findById(1);
		verify(logger).info("find owner {}", 1);
	}

	@Test
	void Method_findOwner_is_returned_if_owner_with_given_id_is_not_found(){
		assertNull(petManager.findOwner(1));
		verify(ownerRepository).findById(1);
		verify(logger).info("find owner {}", 1);
	}

	@Test
	void New_pet_is_created_and_is_owned_by_expected_owner(){
		Pet expectedPet = petManager.newPet(owner);
		assertNotNull(expectedPet);
		verify(owner).addPet(expectedPet);
		verify(logger).info("add pet for owner {}", owner.getId());

	}

	@Test
	void Method_findPet_returns_the_correct_pet_with_given_id(){
		Pet expectedPet = new Pet();
		when(petTimedCache.get(1)).thenReturn(expectedPet);
		Pet actualPet = petManager.findPet(1);
		assertNotNull(actualPet);
		assertEquals(actualPet, expectedPet);
		verify(petTimedCache).get(1);
		verify(logger).info("find pet by id {}", 1);
	}

	@Test
	void Method_findPet_returns_Null_if_pet_with_given_id_is_not_found(){
		Pet actualPet = petManager.findPet(1);
		assertNull(actualPet);
		verify(petTimedCache).get(1);
		verify(logger).info("find pet by id {}", 1);
	}

	@Test
	void Pet_is_saved_in_cache_and_is_owned_by_owner(){
		Pet expectedPet = mock(Pet.class);
		when(expectedPet.getId()).thenReturn(1);
		petManager.savePet(expectedPet, owner);
		verify(logger).info("save pet {}", 1);
		verify(owner).addPet(expectedPet);
		verify(petTimedCache).save(expectedPet);
	}



}
