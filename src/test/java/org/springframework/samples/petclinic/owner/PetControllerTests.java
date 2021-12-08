package org.springframework.samples.petclinic.owner;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PetController.class,
	includeFilters = {
		@ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetService.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = LoggerConfig.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetTimedCache.class, type = FilterType.ASSIGNABLE_TYPE),
	}
)
class PetControllerTests {
	@Autowired
	private MockMvc mvc;
	@MockBean
	private OwnerRepository ownerRepository;
	@MockBean
	private PetRepository petRepository;

	private static final String PREFIX = "/owners/1/";
	private static final String CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
	private Owner owner;
	private Pet pet;
	private PetType dog;

	@BeforeEach
	public void setUp(){
		owner = new Owner();
		dog = new PetType();
		dog.setName("dog");
		pet = new Pet();
		pet.setName("Pet1");
		pet.setType(dog);
		given(this.petRepository.findPetTypes()).willReturn(Lists.newArrayList(dog));
		given(this.ownerRepository.findById(1)).willReturn(owner);
	}

	@Test
	public void createOrUpdateFormIsReturnedWhenOwnerExistsAndWantsToCreateNewPets() throws Exception{
		given(ownerRepository.findById(1)).willReturn(owner);
		ResultActions resultActions = mvc.perform(get(PREFIX + "pets/new")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_OR_UPDATE_FORM))
				.andExpect(model().attributeExists("pet"));
	}

	@Test
	public void whenThereIsNoErrorsInBindingsNewPetIsAddedAndRedirectedToTheFirstPage() throws Exception{
		ResultActions resultActions = mvc.perform(post(PREFIX + "pets/new")
				.param("name", "Pet1")
				.param("type", "dog")
				.param("id", "1")
				.param("birthDate", "2019-11-18"))
				.andExpect(status().is3xxRedirection())
				.andExpect(model().attributeDoesNotExist("pet"));
	}

//	@Test
//	public void whenThereIsNoErrorsInBindingsNewPetIsAddedAndRedirectedToTheFirstPage1() throws Exception{
//		owner.addPet(pet);
//		ResultActions resultActions = mvc.perform(post(PREFIX + "pets/new")
//				.param("name", "Pet1")
//				.param("type", "dog")
//				.param("id", "")
//				.param("birthDate", "2019-11-18"))
//				.andExpect(status().isOk())
//				.andExpect(model().attributeExists("pet"));
//	}

}
