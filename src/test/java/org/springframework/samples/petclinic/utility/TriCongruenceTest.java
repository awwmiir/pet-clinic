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
	@ClauseCoverage(predicate = "x + y",
					valuations = {
					@Valuation(clause = 'x', valuation = true),
					@Valuation(clause = 'y', valuation = true)
					})
	@Test
	public void trianglesAreNotCongruentWhenTheirSmallestSideIsNegative(){
		Triangle t1 = new Triangle(9, 8, -1);
		Triangle t2 = new Triangle(-1, 8, 9);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/*
	 * Clause Coverage
	 * x = False -> a1 >= 0
	 * y = True -> a1 + b1 < c1
	 */
	@ClauseCoverage(predicate = "x + y",
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = true)
		})
	@Test
	public void trianglesAreNotCongruentWhenSmallestSideIsPositiveButSumOfSmallestAndMiddleSidesAreLessThanLargestSide(){
		Triangle t1 = new Triangle(15, 8, 6);
		Triangle t2 = new Triangle(8, 6, 15);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/*
	 * Clause Coverage
	 * x = False -> a1 >= 0
	 * y = False -> a1 + b1 >= c1
	 *
	 * Correlated Active Clause Coverage
	 * Major Clause is x
	 * x = False -> a1 >= 0
	 * when x is False then p should be False
	 * and when x is True then p should be True
	 * then when x is the major clause and x is False, y should be False too
	 * y = False -> a1 + b1 >= c1
	 */
	@ClauseCoverage(predicate = "x + y",
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = false)
		})
	@CACC(
		predicate = "x + y", majorClause = 'x', valuations = {
		@Valuation(clause = 'x', valuation = false),
		@Valuation(clause = 'y', valuation = false)
	}, predicateValue = false)
	@Test
	public void trianglesAreCongruentWhenSmallestSideIsPositiveAndSumOfSmallestAndMiddleSidesAreLargerThanLargestSide(){
		Triangle t1 = new Triangle(10, 8, 6);
		Triangle t2 = new Triangle(8, 6, 10);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	/*
	 * Correlated Active Clause Coverage
	 * Major Clause is y
	 * y = False -> a1 + b1 >= c1
	 * when y is False then p should be False
	 * and when y is True then p should be True
	 * then when y is the major clause and y is False, x should be False too
	 * x = False -> a1 >= 0
	 */
	@CACC(
		predicate = "x + y", majorClause = 'y', valuations = {
		@Valuation(clause = 'x', valuation = false),
		@Valuation(clause = 'y', valuation = false)
	}, predicateValue = false)
	@Test
	public void trianglesAreCongruentWhenSmallestSideIsPositiveAndSumOfSmallestAndMiddleSidesAreLargerThanLargestSideWithSecondClauseAsMajorClause(){
		Triangle t1 = new Triangle(3, 4, 5);
		Triangle t2 = new Triangle(4, 5, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	/*
	 * Correlated Active Clause Coverage
	 * Major Clause is y
	 * y = True -> a1 + b1 < c1
	 * when y is True then p should be True
	 * and when y is False then p should be False
	 * then when y is the major clause and y is True, x should be False
	 * x = False -> a1 >= 0
	 */
	@CACC(
		predicate = "x + y", majorClause = 'y', valuations = {
		@Valuation(clause = 'x', valuation = false),
		@Valuation(clause = 'y', valuation = true)
	}, predicateValue = true)
	@Test
	public void trianglesAreNotCongruentWhenSmallestSideIsPositiveButSumOfSmallestAndMiddleSidesAreLessThanLargestSideWithSecondClauseAsMajorClause(){
		Triangle t1 = new Triangle(3, 4, 8);
		Triangle t2 = new Triangle(4, 8, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/**
	 * DNF = = ab + cd
	 * p UTPs = {TTFFF, FFTTT}
	 * !p UTPs = {FTFTT, FTTFT, TFFTT, TFTFT}
	 * UTPC TR = {TTFFF, FFTTT, FTFTT, FTTFT, TFFTT, TFTFT}
	 * CUTPNFP TR = {TTFTT, TFFTT, FTFTT, FTTTT, FTTFT}
	 * as you can see the test requirements of CUTPNFP has 5 members and test requirements of UTPC has 6 members
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = false;
		predicate = (a && b) || (c && d && e) || (c && d && !e);
		return predicate;
	}
}
