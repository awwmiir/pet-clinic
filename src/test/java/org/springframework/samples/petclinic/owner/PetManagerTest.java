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

	@BeforeEach
	void setUp(){
		petManager = new PetManager(petTimedCache, ownerRepository, logger);
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
	void Null_is_returned_if_owner_with_given_id_is_not_found(){
		assertNull(petManager.findOwner(1));
		verify(ownerRepository).findById(1);
	}

	@Test
	void New_pet_is_created_and_is_owned_by_expected_owner(){
		Pet expectedPet = petManager.newPet(owner);
		assertNotNull(expectedPet);
		verify(owner).addPet(expectedPet);
	}

	@Test
	void Method_findPet_returns_the_correct_pet_with_given_id(){
		Pet expectedPet = new Pet();
		when(petTimedCache.get(1)).thenReturn(expectedPet);
		Pet actualPet = petManager.findPet(1);
		assertNotNull(actualPet);
		assertEquals(actualPet, expectedPet);
		verify(petTimedCache).get(1);
	}
}
