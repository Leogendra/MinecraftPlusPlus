package fr.minecraftpp.core.set.material;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * Draws the armor statistics of a material, with the formulas and the draw order of the 1.12 MaterialSet.
 */
public final class ArmorStatsGenerator
{
	private static final Map<ArmorPiece, Float> BASE_DEFENSE = Map.of(ArmorPiece.HELMET, 1.0F, ArmorPiece.CHESTPLATE, 2.3F, ArmorPiece.LEGGINGS, 2.8F, ArmorPiece.BOOTS, 1.1F);

	private ArmorStatsGenerator()
	{
	}

	public static ArmorStats generate(Random rand, int tier)
	{
		int durabilityFactor = (tier + 1) * (rand.nextInt(9) + 5);
		Map<ArmorPiece, Integer> defense = generateDefense(rand, tier);
		float toughness = rand.nextFloat() * tier;

		return new ArmorStats(durabilityFactor, defense, toughness);
	}

	private static Map<ArmorPiece, Integer> generateDefense(Random rand, int tier)
	{
		float factor = (((8 * tier) + rand.nextInt(10)) / 10.0F) + 1;
		Map<ArmorPiece, Integer> defense = new EnumMap<>(ArmorPiece.class);

		for (ArmorPiece piece : ArmorPiece.values())
		{
			defense.put(piece, Math.round(BASE_DEFENSE.get(piece) * factor));
		}

		return defense;
	}
}
