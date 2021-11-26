package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportingExtension.class)
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	@Test
	public void sampleTest() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(7, 2, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}
	/*
	 * for the following CUTPNFP tests we have:
	 * p = (a1 != a2) || (b1 != b2) || (c1 != c2)
	 * p = x + y + z [DNF]
	 */

	/*
	 * x = True, y = False and z = False
  	 * a1 != a2,
  	 * b1 = b2
  	 * c1 = c2
	 */
	@Test
	public void trianglesAreNotCongruentWhenTheirSmallestSidesAreNotEqualAndTheOtherTwoSidesAreEqual(){
		Triangle t1 = new Triangle(10, 8, 6);
		Triangle t2 = new Triangle(8, 7, 10);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/*
	 * x = False, y = True and z = False
	 * a1 = a2
	 * b1 != b2
	 * c1 = c2
	 * */
	@Test
	public void trianglesAreNotCongruentWhenTheirMiddleSidesAreNotEqualAndTheOtherTwoSidesAreEqual(){
		Triangle t1 = new Triangle(10, 7, 6);
		Triangle t2 = new Triangle(8, 6, 10);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/*
	 * x = False, y = False and z = True
	 * a1 = a2
	 * b1 = b2
	 * c1 != c2
	 */
	@Test
	public void trianglesAreNotCongruentWhenTheirLargestSidesAreNotEqualAndTheOtherTwoSidesAreEqual(){
		Triangle t1 = new Triangle(9, 8, 6);
		Triangle t2 = new Triangle(8, 6, 10);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/*
	 * for the following test we have:
	 * p = (a1 < 0) || (a1 + b1 < c1)
	 * p = x || y
	*/

	/*
	 * Clause Coverage
	 * x = True -> a1 < 0
	 * y = True -> a1 + b1 < c1
	*/
	@Test
	public void trianglesAreNotCongruentWhenTheirSmallestSideIsNegative(){
		Triangle t1 = new Triangle(9, 8, -1);
		Triangle t2 = new Triangle(-1, 6, 10);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/*
	 * Clause Coverage
	 * x = False -> a1 >= 0
	 * y = True -> a1 + b1 < c1
	 */
	@Test
	public void trianglesAreNotCongruentWhenSumOfSmallestAndMiddleSidesAreLessThanLargestSide(){
		Triangle t1 = new Triangle(15, 8, 6);
		Triangle t2 = new Triangle(3, 6, 10);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}
	/**
	 * TODO
	 * explain your answer here
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = false;
//		predicate = a predicate with any number of clauses
		return predicate;
	}
}
