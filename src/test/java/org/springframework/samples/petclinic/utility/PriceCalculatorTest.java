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
	private static final int INFANT_YEARS = 2;

	private List<Pet> petList;
	private List<Visit> visitList;


	@BeforeEach
	void setUp() {
		petList = new ArrayList<>();
		visitList = new ArrayList<>();
	}

	void setUpVisits(){
		Visit visit1 = mock(Visit.class);
		when(visit1.getDate()).thenReturn(LocalDate.now().minusDays(90));

		Visit visit2 = mock(Visit.class);
		when(visit2.getDate()).thenReturn(LocalDate.now().minusDays(110));

		Visit visit3 = mock(Visit.class);
		when(visit3.getDate()).thenReturn(LocalDate.now().minusDays(100));

		visitList.add(visit1);
		visitList.add(visit2);
		visitList.add(visit3);
	}

	void setUpPets(){
		for(int i = 0; i < 20; i++){
			Pet pet = new Pet();
			pet.setBirthDate(LocalDate.now().minusYears((i % 3) * INFANT_YEARS));
			if(i % 5  == 0)
				pet.addVisit(visitList.get(0));
			else if(i % 5 == 1)
				pet.addVisit(visitList.get(1));
			else if(i % 5 == 2)
				pet.addVisit(visitList.get(2));
			else if(i % 5 == 3){
				pet.addVisit(visitList.get(((i % 3) + 1) % 3));
				pet.addVisit(visitList.get(i % 3));
			}
			petList.add(pet);
		}
	}

	@Test
	void testCalcPriceMethod(){
		setUpVisits();
		setUpPets();
		assertEquals(7696227.6, PriceCalculator.calcPrice(petList, BASE_CHARGE, BASE_PRICE_PER_PET), DELTA);
	}

}
