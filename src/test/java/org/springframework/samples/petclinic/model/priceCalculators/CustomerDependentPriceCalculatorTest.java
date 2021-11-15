package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerDependentPriceCalculatorTest {

	private CustomerDependentPriceCalculator customerDependentPriceCalculator;
	private List<Pet> pets;
	private Date birthDateNotInfant, birthDateInfant;
	private Pet pet1, pet2, pet3, pet4, pet5;
	private PetType petType;

	public static Double BASE_CHARGE = 10.0, BASE_PRICE_PER_PET = 5.0;

	void setUp(){
		customerDependentPriceCalculator = new CustomerDependentPriceCalculator();
		petType = mock(PetType.class);
		when(petType.getRare()).thenReturn(true);
		pets = new ArrayList<>();
		birthDateNotInfant = new Calendar.Builder()
			.setDate(2017, 1, 1)
			.build()
			.getTime();

		birthDateInfant = new Calendar.Builder()
			.setDate(2020, 1, 1)
			.build()
			.getTime();

		pet1 = new Pet();
		pet1.setType(petType);
		pet1.setBirthDate(birthDateInfant);

		pet2 = new Pet();
		pet2.setType(petType);
		pet2.setBirthDate(birthDateInfant);

		pet3 = new Pet();
		pet3.setType(petType);
		pet3.setBirthDate(birthDateInfant);

		pet4 = new Pet();
		pet4.setType(petType);
		pet4.setBirthDate(birthDateInfant);

		pet5 = new Pet();
		pet5.setType(petType);
		pet5.setBirthDate(birthDateInfant);

		pets.add(pet1);
		pets.add(pet2);
		pets.add(pet3);
		pets.add(pet4);
		pets.add(pet5);

	}

	@Test
	public void newUsersWithEmptyListsAreNotCharged(){
		this.setUp();
		assertEquals(
			0,
			customerDependentPriceCalculator.calcPrice(new ArrayList<>(), BASE_CHARGE, BASE_PRICE_PER_PET, UserType.NEW),
			0.0);
	}

	@Test
	public void silverUsersWithEmptyListsAreNotCharged(){
		this.setUp();
		assertEquals(
			0,
			customerDependentPriceCalculator.calcPrice(new ArrayList<>(), BASE_CHARGE, BASE_PRICE_PER_PET, UserType.SILVER),
			0.0);
	}

	@Test
	public void goldUsersWithEmptyListAreChargedWithBaseCharge(){
		this.setUp();
		assertEquals(
			BASE_CHARGE,
			customerDependentPriceCalculator.calcPrice(new ArrayList<>(), BASE_CHARGE, BASE_PRICE_PER_PET, UserType.GOLD),
			0.0);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientIsUsedForRarePetsAndTotalPriceIsMultipliedByDiscountRatePlusBaseChargeForNewUsersWithEnoughDiscountScore(){
		this.setUp();
		assertEquals(49.9,
			customerDependentPriceCalculator.calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET, UserType.NEW),
			0.1);
	}

	@Test
	public void basePricePerPetIsUsedForCommonPetsAndTotalPriceForNewUsersWithNotEnoughDiscountScore(){
		this.setUp();
		when(petType.getRare()).thenReturn(false);
		assertEquals(30.0,
			customerDependentPriceCalculator.calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET, UserType.NEW),
			0.1);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientIsUsedForRarePetsAndTotalPriceIsAddedToTheBaseChargeAndMultipliedByDiscountRateForSilverUsersWithEnoughDiscountScore(){
		this.setUp();
		assertEquals(46.8,
			customerDependentPriceCalculator.calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET, UserType.SILVER),
			0.1);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientIsUsedForRarePetsAndTotalPriceIsMultipliedByDiscountRatePlusBaseChargeForGoldUsersWithNotEnoughDiscountScore(){
		this.setUp();
		pets.remove(4);
		assertEquals(36.88,
			customerDependentPriceCalculator.calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET, UserType.GOLD),
			0.1);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientIsUsedForRarePetsAndDiscountRateIsIncreasedByOneIfPetIsNotRareTotalPriceIsMultipliedByDiscountRatePlusBaseChargeForGoldUsersWithNotEnoughDiscountScore(){
		this.setUp();
		PetType petType1 = mock(PetType.class);
		when(petType1.getRare()).thenReturn(false);
		Pet newPet = new Pet();
		newPet.setBirthDate(birthDateInfant);
		newPet.setType(petType1);
		pets.add(newPet);

		pets.remove(4);
		assertEquals(41.64,
			customerDependentPriceCalculator.calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET, UserType.GOLD),
			0.1);
	}

}
