package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.utility.SimpleDI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class PetManagerTest {
	@MockBean
	private PetTimedCache petTimedCache;
	@MockBean
	private OwnerRepository ownerRepository;
	@MockBean
	private Logger logger;
	private PetManager petManager;

	@BeforeEach
	void setUp(){
		petManager = new PetManager(petTimedCache, ownerRepository, logger);
	}

	@Test
	void findOwner_method_returns_the_correct_owner_with_the_requested_id(){
		Owner expectedOwner = new Owner();
		when(ownerRepository.findById(1)).thenReturn(expectedOwner);
		Owner actualOwner = petManager.findOwner(1);
		assertNotNull(actualOwner);
		assertEquals(actualOwner, expectedOwner);
		verify(ownerRepository).findById(1);
	}
}
