package fr.minecraftpp.core.trait;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * The chance of each random trait: the single source of the trait probabilities used by the set generators (decision D13).
 *
 * The parameters of each trait (food values, opacity, damage...) are still drawn by the generators with the 1.12 formulas. They join this catalog with the configuration file planned after the migration.
 */
public record TraitCatalog(Map<Trait, Integer> oneIn)
{
	public TraitCatalog
	{
		EnumMap<Trait, Integer> copy = new EnumMap<>(Trait.class);
		copy.putAll(oneIn);

		for (Trait trait : Trait.values())
		{
			Integer chance = copy.get(trait);

			if (chance == null)
			{
				throw new IllegalArgumentException("Missing chance for trait " + trait.getId());
			}
			else if (chance < 1)
			{
				throw new IllegalArgumentException("The chance of trait " + trait.getId() + " must be at least 1: " + chance);
			}
		}

		oneIn = Collections.unmodifiableMap(copy);
	}

	/**
	 * The chances of the 1.12 version.
	 */
	public static TraitCatalog defaults()
	{
		Map<Trait, Integer> chances = new EnumMap<>(Trait.class);

		for (Trait trait : Trait.values())
		{
			chances.put(trait, trait.getDefaultOneIn());
		}

		return new TraitCatalog(chances);
	}

	public int oneIn(Trait trait)
	{
		return this.oneIn.get(trait);
	}

	/**
	 * Draws whether a set receives the trait, with a single nextInt call whatever the result, as in 1.12.
	 */
	public boolean draw(Trait trait, Random rand)
	{
		return rand.nextInt(this.oneIn(trait)) == 0;
	}
}
