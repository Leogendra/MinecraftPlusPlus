package fr.minecraftpp.core.set;

/**
 * Validation of the numeric values of the set definitions.
 */
final class Bounds
{
	private Bounds()
	{
	}

	static void check(String name, double value, double minimum, double maximum)
	{
		if (value < minimum || value > maximum)
		{
			throw new IllegalArgumentException(name + " must be between " + minimum + " and " + maximum + ": " + value);
		}
	}
}
