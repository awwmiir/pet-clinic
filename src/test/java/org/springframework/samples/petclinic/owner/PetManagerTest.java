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

import java.util.*;

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

	@Test
	void Method_findOwner_returns_the_correct_owner_with_the_requested_id(){
		when(ownerRepository.findById(OWNER_ID)).thenReturn(owner);
		Owner actualOwner = petManager.findOwner(OWNER_ID);
		assertNotNull(actualOwner);
		assertEquals(actualOwner, owner);
		verify(ownerRepository).findById(OWNER_ID);
		verify(logger).info("find owner {}", OWNER_ID);
	}

	@Test
	void Method_findOwner_is_returned_if_owner_with_given_id_is_not_found(){
		assertNull(petManager.findOwner(OWNER_ID));
		verify(ownerRepository).findById(OWNER_ID);
		verify(logger).info("find owner {}", OWNER_ID);
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
		when(petTimedCache.get(PET_ID)).thenReturn(expectedPet);
		Pet actualPet = petManager.findPet(PET_ID);
		assertNotNull(actualPet);
		assertEquals(actualPet, expectedPet);
		verify(petTimedCache).get(PET_ID);
		verify(logger).info("find pet by id {}", PET_ID);
	}

	@Test
	void Method_findPet_returns_Null_if_pet_with_given_id_is_not_found(){
		Pet actualPet = petManager.findPet(PET_ID);
		assertNull(actualPet);
		verify(petTimedCache).get(PET_ID);
		verify(logger).info("find pet by id {}", PET_ID);
	}

	@Test
	void Pet_is_saved_in_cache_and_is_owned_by_owner(){
		Pet expectedPet = mock(Pet.class);
		when(expectedPet.getId()).thenReturn(PET_ID);
		petManager.savePet(expectedPet, owner);
		verify(logger).info("save pet {}", PET_ID);
		verify(owner).addPet(expectedPet);
		verify(petTimedCache).save(expectedPet);
	}

	@Test
	void Owner_pets_are_returned_correctly(){
		when(owner.getPets()).thenReturn(pets);
		when(ownerRepository.findById(OWNER_ID)).thenReturn(owner);
		assertEquals(petManager.getOwnerPets(OWNER_ID), pets);
		verify(ownerRepository).findById(OWNER_ID);
		verify(owner).getPets();
		verify(logger).info("finding the owner's pets by id {}", OWNER_ID);
	}

	@Test
	void Owner_pet_types_are_returned_correctly(){
		when(ownerRepository.findById(OWNER_ID)).thenReturn(owner);
		when(owner.getPets()).thenReturn(pets);
		assertEquals(petManager.getOwnerPetTypes(OWNER_ID), petTypes);
		verify(ownerRepository).findById(OWNER_ID);
		verify(owner).getPets();
		verify(logger).info("finding the owner's petTypes by id {}", OWNER_ID);
	}
}
