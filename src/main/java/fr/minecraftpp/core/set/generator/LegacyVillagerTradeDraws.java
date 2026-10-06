package fr.minecraftpp.core.set.generator;

import java.util.List;
import java.util.Random;

/**
 * Reproduces a hidden draw of the 1.12 version, so that a seed keeps giving the same sets.
 *
 * In 1.12, the currency role called a static method of EntityVillager. The first call initialized the villager trade table, which looked up a random variant of 22 vanilla items, in the order below, with the random source of the ore generation. Each lookup of an item that already had variants drew one integer. The chosen variants are not used anymore: in 26.1.2 the trades are rewritten from data (commit 4.7).
 */
final class LegacyVillagerTradeDraws
{
	private static final List<String> VARIANT_LOOKUPS = List.of("minecraft:coal", "minecraft:gold_ingot", "minecraft:coal", "minecraft:iron_helmet", "minecraft:iron_ingot", "minecraft:iron_chestplate", "minecraft:diamond", "minecraft:diamond_chestplate", "minecraft:coal", "minecraft:iron_axe", "minecraft:iron_ingot", "minecraft:iron_sword", "minecraft:diamond", "minecraft:diamond_sword", "minecraft:diamond_axe", "minecraft:coal", "minecraft:iron_shovel", "minecraft:iron_ingot", "minecraft:iron_pickaxe", "minecraft:diamond", "minecraft:diamond_pickaxe", "minecraft:coal");

	private LegacyVillagerTradeDraws()
	{
	}

	static void draw(Random rand, GenerationContext context)
	{
		if (context.initializeVillagerTrades())
		{
			for (String vanillaItemId : VARIANT_LOOKUPS)
			{
				int variantCount = context.variantCount(vanillaItemId);

				if (variantCount > 0)
				{
					rand.nextInt(variantCount);
				}
			}
		}
	}
}
