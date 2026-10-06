package fr.minecraftpp.core.solver.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BooleanExpressionTest
{
	@Test
	void comparesIntegers()
	{
		assertTrue(BooleanExpression.evaluate("3 == 3"));
		assertFalse(BooleanExpression.evaluate("3 == 4"));
	}

	@Test
	void evaluatesTheCoverageExpressionOfPretreatment()
	{
		assertTrue(BooleanExpression.evaluate("((1 == 1) || (2 == 1)) && ((1 == 2) || (2 == 2))"));
		assertFalse(BooleanExpression.evaluate("((1 == 1) || (1 == 1)) && ((1 == 2) || (1 == 2))"));
	}

	@Test
	void evaluatesTheGroupExpressionOfPretreatment()
	{
		assertTrue(BooleanExpression.evaluate("((5 == 3) || (3 == 3))"));
		assertFalse(BooleanExpression.evaluate("((5 == 3) || (4 == 3))"));
	}

	@Test
	void andBindsTighterThanOr()
	{
		assertTrue(BooleanExpression.evaluate("1 == 1 || 1 == 2 && 1 == 2"));
		assertFalse(BooleanExpression.evaluate("(1 == 1 || 1 == 2) && 1 == 2"));
	}

	@Test
	void rejectsMalformedExpressions()
	{
		assertThrows(IllegalArgumentException.class, () -> BooleanExpression.evaluate("()"));
		assertThrows(IllegalArgumentException.class, () -> BooleanExpression.evaluate("(1 == 1"));
		assertThrows(IllegalArgumentException.class, () -> BooleanExpression.evaluate("1 == 1)"));
		assertThrows(IllegalArgumentException.class, () -> BooleanExpression.evaluate("$iron == 1"));
	}
}
