package fr.minecraftpp.core.set;

/**
 * What an ore block drops when it is mined with the right tool.
 */
public sealed interface OreDrop permits OreDrop.Itself, OreDrop.Items
{
	/**
	 * The ore drops itself and must be smelted, like the vanilla metal ores of 1.12.
	 */
	record Itself() implements OreDrop
	{
	}

	/**
	 * The ore drops between {@code minimum} and {@code maximum} items of its set, and experience, like the vanilla gem ores.
	 */
	record Items(int minimum, int maximum, int minimumExperience, int maximumExperience) implements OreDrop
	{
		public Items
		{
			Bounds.check("minimum drop", minimum, 1, maximum);
			Bounds.check("minimum experience", minimumExperience, 0, maximumExperience);
		}

		/**
		 * The integer average used by the 1.12 version to scale the blue dye and redstone recipes.
		 */
		public int averageQuantity()
		{
			return (this.maximum + this.minimum) / 2;
		}
	}
}
