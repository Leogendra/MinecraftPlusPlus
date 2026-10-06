package fr.minecraftpp.core.solver.engine.constraint;

import java.io.BufferedReader;

import fr.minecraftpp.core.solver.engine.Assignment;
import fr.minecraftpp.core.solver.util.BooleanExpression;

public class ConstraintIntension extends ConstraintTotale
{

	private String expression;

	public ConstraintIntension(BufferedReader in) throws Exception
	{
		super(in);
		expression = in.readLine();
	}

	@Override
	protected boolean verifierValeurs(Assignment a)
	{
		String aEval = expression;
		for (String variable : varList)
		{
			aEval = aEval.replace("$" + variable, a.get(variable) + "");
		}

		boolean evaluer = BooleanExpression.evaluate(aEval);
		return !evaluer;
	}

	@Override
	public String toString()
	{
		return "\n\t Intension " + super.toString() + ": " + expression;
	}
}
