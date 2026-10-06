package fr.minecraftpp.core.solver.engine.constraint;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.solver.engine.Assignment;

class ConstraintTest
{
	@Test
	void differenceIsViolatedByTwoEqualValues() throws Exception
	{
		Constraint difference = Constraint.getInstance("diff", reader("iron;gold;diamond"));

		assertFalse(difference.violation(assignment("iron", 1, "gold", 2, "diamond", 3)));
		assertTrue(difference.violation(assignment("iron", 1, "gold", 2, "diamond", 1)));
	}

	@Test
	void differenceIgnoresUnassignedVariables() throws Exception
	{
		Constraint difference = Constraint.getInstance("diff", reader("iron;gold;diamond"));

		assertFalse(difference.violation(assignment("iron", 1, "gold", 2)));
	}

	@Test
	void intensionIsCheckedOnlyOnceAllItsVariablesAreAssigned() throws Exception
	{
		Constraint intension = Constraint.getInstance("inten", reader("coal;fuel1\n(($fuel1 == $coal))"));

		assertFalse(intension.violation(assignment("coal", 1)));
		assertFalse(intension.violation(assignment("coal", 1, "fuel1", 1)));
		assertTrue(intension.violation(assignment("coal", 1, "fuel1", 2)));
	}

	private static BufferedReader reader(String text)
	{
		return new BufferedReader(new StringReader(text));
	}

	private static Assignment assignment(Object... variablesAndValues)
	{
		Assignment assignment = new Assignment();

		for (int i = 0; i < variablesAndValues.length; i += 2)
		{
			assignment.put((String) variablesAndValues[i], (Integer) variablesAndValues[i + 1]);
		}

		return assignment;
	}
}
