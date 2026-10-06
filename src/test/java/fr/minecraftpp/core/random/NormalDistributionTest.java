package fr.minecraftpp.core.random;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

class NormalDistributionTest
{
	private static final double PRECISION = 1e-6;

	@Test
	void cumulativeProbabilityOfTheMeanIsOneHalf() throws Exception
	{
		NormalDistribution distribution = new NormalDistribution(new Random(0), 127, 74);

		assertEquals(0.5, distribution.cumulativeProbability(127), PRECISION);
	}

	@Test
	void cumulativeProbabilityMatchesKnownValuesOfTheStandardNormalLaw() throws Exception
	{
		NormalDistribution distribution = new NormalDistribution(new Random(0), 0, 1);

		assertEquals(0.841344746, distribution.cumulativeProbability(1), PRECISION);
		assertEquals(0.975002105, distribution.cumulativeProbability(1.96), PRECISION);
		assertEquals(0.022750132, distribution.cumulativeProbability(-2), PRECISION);
	}
}
