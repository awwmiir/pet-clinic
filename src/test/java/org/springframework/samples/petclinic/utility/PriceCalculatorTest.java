package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PriceCalculatorTest {

	public static final double BASE_CHARGE = 10;
	public static final double BASE_PRICE_PER_PET = 10;
	public static final double DELTA = 0.01;

	private List<Pet> petList;
	private List<Visit> visitList;
	private Visit visit;
	private Pet pet;

	@BeforeEach
	void setUp() {
		petList = new ArrayList<>();
		visitList = new ArrayList<>();
		visit = mock(Visit.class);
		visitList.add(visit);
		pet = mock(Pet.class);
		when(visit.getDate()).thenReturn(LocalDate.of(2017, 1, 1));
	}

	@Test
	public void totalPriceIsZeroForAnEmptyListOfPets() {
		assertEquals(0, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET));
	}

	@Test
	void testOnePetWithoutAnyVisitsWithAgeLessThanInfantYears() {
		petList.add(pet);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		when(pet.getVisitsUntilAge(1)).thenReturn(new ArrayList<>());
		assertEquals(16.8, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testOnePetWithVisitsWithAgeLessThanInfantYears() {
		petList.add(pet);
		when(pet.getVisitsUntilAge(1)).thenReturn(visitList);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		assertEquals(16.8, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testOnePetWithoutAnyVisitsWithAgeMoreThanInfantYears() {
		petList.add(pet);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2017, 1, 1));
		when(pet.getVisitsUntilAge(4)).thenReturn(new ArrayList<>());
		assertEquals(12, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testOnePetWithVisitsWithAgeMoreThanInfantYears() {
		petList.add(pet);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2017, 1, 1));
		when(pet.getVisitsUntilAge(4)).thenReturn(visitList);
		assertEquals(12, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testFivePetsWithoutAnyVisitsWithAgeLessThanInfantYearsWithDiscountCounterMoreThan10() {
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		when(pet.getVisitsUntilAge(1)).thenReturn(new ArrayList<>());
		for (int i = 0; i < 5; i++)
			petList.add(pet);
		assertEquals(161.2, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testFivePetsWithoutAnyVisitsWithAgeMoreThanInfantYearsWithDiscountCounterMoreThan10() {
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2017, 1, 1));
		when(pet.getVisitsUntilAge(4)).thenReturn(new ArrayList<>());
		for (int i = 0; i < 5; i++)
			petList.add(pet);
		assertEquals(60, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testFivePetsWithVisitsMoreThan100DaysWithAgeLessThanInfantYearsWithDiscountCounterMoreThan10() {
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		when(pet.getVisitsUntilAge(1)).thenReturn(visitList);
		for (int i = 0; i < 5; i++)
			petList.add(pet);
		assertEquals(1483.6, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testFivePetsWithVisitsLessThan100DaysWithAgeLessThanInfantYearsWithDiscountCounterMoreThan10() {
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		visitList = new ArrayList<>();
		when(visit.getDate()).thenReturn(LocalDate.of(2021, 10,10));
		visitList.add(visit);
		when(pet.getVisitsUntilAge(1)).thenReturn(visitList);
		for (int i = 0; i < 5; i++)
			petList.add(pet);
		assertEquals(161.2, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testSixPetsWithVisitsMoreThan100DaysAndOneLessThan100DaysWithAgeLessThanInfantYearsWithDiscountCounterMoreThan10() {
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		when(visit.getDate()).thenReturn(LocalDate.of(2021, 10,10));
		when(pet.getVisitsUntilAge(1)).thenReturn(visitList);
		Pet pet2 = mock(Pet.class);
		List<Visit> visitList2 = new ArrayList<>();
		Visit visit2 = mock(Visit.class);
		when(visit2.getDate()).thenReturn(LocalDate.of(2016, 1,1));
		when(pet2.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		visitList2.add(visit2);
		when(pet2.getVisitsUntilAge(1)).thenReturn(visitList2);

		for (int i = 0; i < 5; i++)
			petList.add(pet);
		petList.add(pet2);
		assertEquals(3783.2, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testSixPetsWithVisitsLessThan100DaysAndOneMoreThan100DaysWithAgeLessThanInfantYearsWithDiscountCounterMoreThan10() {
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		when(pet.getVisitsUntilAge(1)).thenReturn(visitList);
		Pet pet2 = mock(Pet.class);
		List<Visit> visitList2 = new ArrayList<>();
		Visit visit2 = mock(Visit.class);
		when(visit2.getDate()).thenReturn(LocalDate.of(2021, 10,10));
		when(pet2.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		visitList2.add(visit2);
		when(pet2.getVisitsUntilAge(1)).thenReturn(visitList2);

		for (int i = 0; i < 5; i++)
			petList.add(pet);
		petList.add(pet2);
		assertEquals(2994, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

	@Test
	void testSevenPetsWithVisitsLessThan100DaysAndOneMoreThan100DaysAndOneWithNoVisitsWithAgeLessThanInfantYearsWithDiscountCounterMoreThan10() {
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		when(pet.getVisitsUntilAge(1)).thenReturn(visitList);
		Pet pet2 = mock(Pet.class);
		List<Visit> visitList2 = new ArrayList<>();
		Visit visit2 = mock(Visit.class);
		when(visit2.getDate()).thenReturn(LocalDate.of(2021, 10,10));
		when(pet2.getBirthDate()).thenReturn(LocalDate.of(2020, 1, 1));
		visitList2.add(visit2);
		when(pet2.getVisitsUntilAge(1)).thenReturn(visitList2);
		Pet pet3 = new Pet();
		pet3.setBirthDate(LocalDate.of(2021, 10,10));
		petList.add(new Pet());
		for (int i = 0; i < 5; i++)
			petList.add(pet);
		petList.add(pet2);
		assertEquals(2994, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

}
