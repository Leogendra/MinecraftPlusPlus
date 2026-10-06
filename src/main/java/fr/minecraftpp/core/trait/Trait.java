package fr.minecraftpp.core.trait;

/**
 * The random traits a set can receive on top of its roles.
 *
 * The declaration order is the order of the draws: changing it changes every set of every seed. The default chances are those of the 1.12 version.
 */
public enum Trait
{
	SHINY("shiny", 20), FIRESTARTER("firestarter", 10), EDIBLE("edible", 7), FALLS("falls", 15), GLOWING("glowing", 7), ABSORBS_WATER("absorbs_water", 50), TRANSLUCENT("translucent", 15), SLIPPERY("slippery", 15), ACCELERATING("accelerating", 17), WALK_DAMAGE("walk_damage", 33), FLAMMABLE("flammable", 33);

	private final String id;
	private final int defaultOneIn;

	Trait(String id, int defaultOneIn)
	{
		this.id = id;
		this.defaultOneIn = defaultOneIn;
	}

	/**
	 * Identifier of the trait in the configuration file planned after the migration.
	 */
	public String getId()
	{
		return this.id;
	}

	/**
	 * A set receives the trait with a probability of one in this value.
	 */
	public int getDefaultOneIn()
	{
		return this.defaultOneIn;
	}
}
