package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SimplePriceCalculatorTest {
	UserType new_user, regular_user;
	Pet regular_pet, rare_pet;

	@MockBean
	private PetType regular_type;

	@MockBean
	private PetType rare_type;

	@Before
	public void setup() {
		new_user = UserType.NEW;
		regular_user = UserType.SILVER;

		regular_type = new PetType();
		when(regular_type.getRare()).thenReturn(false);

		rare_type = new PetType();
		when(rare_type.getRare()).thenReturn(true);

		regular_pet = new Pet();
		regular_pet.setType(regular_type);

		rare_pet = new Pet();
		rare_pet.setType(rare_type);
	}

	@Test
	public void no_pet_regular_user() {
		List<Pet> pets = new ArrayList<Pet>();
		double price = new SimplePriceCalculator().calcPrice(pets, 10.0, 5.0, regular_user);
		System.out.println(price);
	}

	@Test
	public void no_pet_new_user() {
		List<Pet> pets = new ArrayList<Pet>();
		double price = new SimplePriceCalculator().calcPrice(pets, 10.0, 5.0, new_user);
		System.out.println(price);
	}

	@Test
	public void test() {
		List<Pet> pets = new ArrayList<Pet>();

	}
}
