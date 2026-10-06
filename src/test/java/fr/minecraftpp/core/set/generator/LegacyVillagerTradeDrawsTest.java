package fr.minecraftpp.core.set.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.VanillaRole;

/**
 * The 1.12 villager trade table consumed one draw per lookup of a vanilla item that already had variants. Without these draws, the sets processed after the currency set differ from 1.12.
 */
class LegacyVillagerTradeDrawsTest
{
	@Test
	void drawsNothingWhenNoVariantExists()
	{
		assertEquals(0, countDraws(new GenerationContext()));
	}

	@Test
	void drawsOncePerLookupOfAnItemWithVariants()
	{
		GenerationContext context = new GenerationContext();
		context.registerVariants(SetType.SIMPLE, VanillaRole.COAL);

		// minecraft:coal is looked up five times by the trade table
		assertEquals(5, countDraws(context));
	}

	@Test
	void materialIronSetsAlsoAnswerTheToolAndArmorLookups()
	{
		GenerationContext context = new GenerationContext();
		context.registerVariants(SetType.MATERIAL, VanillaRole.IRON);

		// iron ingot three times, plus helmet, chestplate, axe, sword, shovel and pickaxe
		assertEquals(9, countDraws(context));
	}

	@Test
	void onlyTheFirstCurrencyInitializesTheTrades()
	{
		GenerationContext context = new GenerationContext();
		context.registerVariants(SetType.SIMPLE, VanillaRole.COAL);
		countDraws(context);

		assertEquals(0, countDraws(context));
	}

	private static int countDraws(GenerationContext context)
	{
		CountingRandom rand = new CountingRandom();
		LegacyVillagerTradeDraws.draw(rand, context);
		return rand.draws;
	}

	private static class CountingRandom extends Random
	{
		private int draws = 0;

		@Override
		public int nextInt(int bound)
		{
			this.draws++;
			return super.nextInt(bound);
		}
	}
}
