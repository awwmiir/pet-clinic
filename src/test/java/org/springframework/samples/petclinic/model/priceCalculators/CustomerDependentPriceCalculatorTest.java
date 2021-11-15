package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.UserType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CustomerDependentPriceCalculatorTest {

	private CustomerDependentPriceCalculator customerDependentPriceCalculator;
	private List<Pet> pets;

	public static Double BASE_CHARGE = 10.0, BASE_PRICE_PER_PET = 5.0;

	void setUp(){
		customerDependentPriceCalculator = new CustomerDependentPriceCalculator();
		pets = new ArrayList<>();
	}

	@Test
	public void newUsersWithEmptyListsAreNotCharged(){
		this.setUp();
		assertEquals(
			0,
			customerDependentPriceCalculator.calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET, UserType.NEW),
			0.0);
	}

	@Test
	public void silverUsersWithEmptyListsAreNotCharged(){
		this.setUp();
		assertEquals(
			0,
			customerDependentPriceCalculator.calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET, UserType.SILVER),
			0.0);
	}

	@Test
	public void goldUsersWithEmptyListAreChargedWithBaseCharge(){
		this.setUp();
		assertEquals(
			BASE_CHARGE,
			customerDependentPriceCalculator.calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET, UserType.GOLD),
			0.0);
	}
}
