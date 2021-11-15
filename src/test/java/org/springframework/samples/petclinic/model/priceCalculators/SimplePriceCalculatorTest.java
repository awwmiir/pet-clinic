package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimplePriceCalculatorTest {
	UserType new_user, regular_user;
	Pet regular_pet, rare_pet;

	private PetType regular_type;

	private PetType rare_type;

	@Before
	public void setup() {
		new_user = UserType.NEW;
		regular_user = UserType.SILVER;

		regular_type = mock(PetType.class);
		when(regular_type.getRare()).thenReturn(false);

		rare_type = mock(PetType.class);
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
		assertEquals(price, 10, 0.01);
	}

	@Test
	public void no_pet_new_user() {
		List<Pet> pets = new ArrayList<Pet>();
		double price = new SimplePriceCalculator().calcPrice(pets, 10.0, 5.0, new_user);
		assertEquals(price, 9.5, 0.01);
	}

	@Test
	public void tow_rare_pets_regular_user() {
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(rare_pet);
		pets.add(rare_pet);
		double price = new SimplePriceCalculator().calcPrice(pets, 10.0, 5.0, regular_user);
		assertEquals(price, 22, 0.01);
	}

	@Test
	public void regular_pet_rare_pet_new_user() {
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(regular_pet);
		pets.add(rare_pet);
		double price = new SimplePriceCalculator().calcPrice(pets, 10.0, 5.0, new_user);
		assertEquals(price, 19.95, 0.01);
	}

	@Test
	public void rare_pet_regular_pet_regular_user() {
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(rare_pet);
		pets.add(regular_pet);
		double price = new SimplePriceCalculator().calcPrice(pets, 10.0, 5.0, regular_user);
		assertEquals(price, 21, 0.01);
	}

	@Test
	public void tow_regular_pets_new_user() {
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(regular_pet);
		pets.add(regular_pet);
		double price = new SimplePriceCalculator().calcPrice(pets, 10.0, 5.0, new_user);
		assertEquals(price, 19, 0.01);
	}
}
