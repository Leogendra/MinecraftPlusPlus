package fr.minecraftpp.core.set.material;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

class ArmorStatsGeneratorTest
{
	private static final float[] BASE_ARMOR_DAMAGE_REDUCTION = { 1.0F, 2.3F, 2.8F, 1.1F };

	@Test
	void drawsTheSameStatisticsAsThe112MaterialSet()
	{
		for (long seed = 0; seed < 50; seed++)
		{
			for (int tier = 0; tier <= 2; tier++)
			{
				ArmorStats stats = ArmorStatsGenerator.generate(new Random(seed), tier);
				Random legacy = new Random(seed);

				// Verbatim formulas of MaterialSet.getGeneratedArmorDurabilityFactor, getGeneratedArmorDamageReduction and getGeneratedToughness in 1.12
				int durabilityFactor = (tier + 1) * (legacy.nextInt(9) + 5);
				float factor = (((8 * tier) + legacy.nextInt(10)) / 10.0F) + 1;
				float toughness = legacy.nextFloat() * tier;

				assertEquals(durabilityFactor, stats.durabilityFactor());
				for (ArmorPiece piece : ArmorPiece.values())
				{
					assertEquals(Math.round(BASE_ARMOR_DAMAGE_REDUCTION[piece.ordinal()] * factor), stats.defense(piece));
				}
				assertEquals(toughness, stats.toughness());
			}
		}
	}

	@Test
	void durabilityOfAPieceIsItsVanillaBaseTimesTheFactor()
	{
		ArmorStats stats = ArmorStatsGenerator.generate(new Random(0), 2);

		assertEquals(16 * stats.durabilityFactor(), stats.durability(ArmorPiece.CHESTPLATE));
		assertEquals(11 * stats.durabilityFactor(), stats.durability(ArmorPiece.HELMET));
	}
}
