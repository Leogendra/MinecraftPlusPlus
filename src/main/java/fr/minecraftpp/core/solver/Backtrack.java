package fr.minecraftpp.core.solver;

import java.util.Map;
import java.util.Random;

import fr.minecraftpp.core.solver.engine.Assignment;
import fr.minecraftpp.core.solver.engine.CSP;
import fr.minecraftpp.core.solver.engine.Network;

public class Backtrack
{
	public static Map<String, Integer> generateSolution(Random rand, int numberOfOres)
	{
		Pretreatment pretreatment = new Pretreatment(rand, numberOfOres);
		return solve(pretreatment.toString(), rand);
	}

	private static Map<String, Integer> solve(String network, Random rand)
	{
		Network myNetwork;
		try
		{
			myNetwork = new Network(network);
		}
		catch (Exception e)
		{
			throw new IllegalStateException("The generated constraint network is malformed", e);
		}

		Assignment solution = new CSP(myNetwork, rand).searchSolution();

		if (solution == null)
		{
			throw new IllegalStateException("The constraint network has no solution");
		}
		else
		{
			return solution;
		}
	}
}
