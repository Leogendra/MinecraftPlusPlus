package fr.minecraftpp.content;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.core.ore.FlammabilityOf;

/**
 * Seed 42: cychotinte is the fuel (1600 ticks), derepess the coal, dium burns like vines.
 */
class FuelAndFlammabilityTest
{
	@Test
	void fuelsBurnTenTimesLongerAsBlocks()
	{
		Map<String, Integer> burnTimes = FuelRegistration.burnTimes(TestCatalogs.generate(42));

		assertEquals(1600, burnTimes.get("cychotinte_ingot"));
		assertEquals(16000, burnTimes.get("cychotinte_block"));
		assertEquals(burnTimes.get("derepess") * 10, burnTimes.get("derepess_block"));
		assertEquals(4, burnTimes.size());
	}

	@Test
	void onlyTheFlammableBlocksAreRegistered()
	{
		assertEquals(Map.of("dium_block", FlammabilityOf.VINE), FlammabilityRegistration.flammableBlocks(TestCatalogs.generate(42)));
	}
}
