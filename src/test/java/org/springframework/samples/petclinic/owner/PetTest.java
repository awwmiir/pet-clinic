package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.visit.Visit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.*;


@RunWith(Theories.class)
public class PetTest {
	private Pet pet;
	private PetType type;
	private Visit visit1, visit2, visit3;
	private Set<Visit> visits;
	public PetTest(){}

	@BeforeEach
	public void setUp(){
		visits = new HashSet<>();
		setUpType();
		setUpVisit();
		setUpPet();
	}

	void setUpType(){
		type = new PetType();
		type.setName("dog");
	}

	void setUpPet(){
		pet = new Pet();
		pet.setName("dog1");
		pet.setBirthDate(LocalDate.parse("2000-01-01"));
		pet.setType(type);
		pet.setId(1);
	}

	void setUpVisit(){
		visit1 = new Visit();
		visit1.setId(1);
		visit1.setDate(LocalDate.parse("2000-01-02"));
		visit1.setPetId(1);
		visit1.setDescription("description for visit1.");
		visits.add(visit1);

		visit2 = new Visit();
		visit2.setId(2);
		visit2.setDate(LocalDate.parse("2000-01-03"));
		visit2.setPetId(1);
		visit2.setDescription("description for visit2.");
		visits.add(visit2);

		visit3 = new Visit();
		visit3.setId(3);
		visit3.setDate(LocalDate.parse("2000-01-04"));
		visit3.setPetId(1);
		visit3.setDescription("description for visit3.");
		visits.add(visit3);
	}
}
