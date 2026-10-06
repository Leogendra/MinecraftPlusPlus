package fr.minecraftpp.core.set.generator;

import java.util.Random;

import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.trait.TraitCatalog;

/**
 * Generates one set in two passes, like the 1.12 sets: every set is constructed first, then the effects of every set are set up. Both passes draw from the same random source, so their order decides the result of a seed.
 */
public interface OreSetGenerator
{
	/**
	 * First pass: draws the values the 1.12 set drew in its constructor (textures, color, harvest level, ore, material).
	 */
	void construct(Random rand);

	/**
	 * Second pass, once every set is constructed: draws the random traits, then applies the roles.
	 *
	 * @param context state shared with the sets processed before this one
	 */
	void setupEffects(Random rand, TraitCatalog traits, GenerationContext context);

	/**
	 * The immutable result, once both passes are done.
	 */
	OreSetDefinition toDefinition(int index);
}
