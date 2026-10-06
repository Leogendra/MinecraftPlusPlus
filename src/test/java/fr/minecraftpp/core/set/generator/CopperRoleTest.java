package fr.minecraftpp.core.set.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.GoldenDetails;
import fr.minecraftpp.core.GoldenFixture;
import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.VanillaRole;

/**
 * Decision D6: copper is given to the only ore the solver leaves without a vanilla role, mined with a stone pickaxe, and its tools mine like the vanilla copper tools, which mine like the stone tools.
 */
class CopperRoleTest
{
	private static final Set<VanillaRole> VANILLA_GROUP = EnumSet.of(VanillaRole.COAL, VanillaRole.IRON, VanillaRole.GOLD, VanillaRole.DIAMOND, VanillaRole.REDSTONE, VanillaRole.CURRENCY);
	private static final List<String> COPPER_DETAILS = List.of("roles", "block.harvest_level", "ore.harvest_level", "material.harvest_level");

	@Test
	void exactlyOneOreWithoutVanillaRoleIsCopper()
	{
		for (long seed = 0; seed < 2_000; seed++)
		{
			List<OreSetDefinition> copperSets = TestCatalogs.generate(seed).sets().stream().filter(set -> set.hasRole(VanillaRole.COPPER)).toList();

			assertEquals(1, copperSets.size(), "seed " + seed);

			OreSetDefinition copper = copperSets.getFirst();
			assertTrue(copper.roles().stream().noneMatch(VANILLA_GROUP::contains), "seed " + seed + ": " + copper.roles());
			assertEquals(HarvestLevel.STONE, copper.ore().harvestLevel(), "seed " + seed);
			assertEquals(HarvestLevel.STONE, copper.block().harvestLevel(), "seed " + seed);

			if (copper.material().isPresent())
			{
				assertEquals(HarvestLevel.STONE, copper.material().get().miningLevel(), "seed " + seed);
			}
		}
	}

	/**
	 * Copper is assigned without any random draw: apart from the copper set's role and harvest levels, every value stays the 1.12 value.
	 */
	@Test
	void copperChangesNothingElseThanTheCopperSet()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			List<OreSetDefinition> withCopper = TestCatalogs.generate(seed).sets();
			List<OreSetDefinition> of112 = TestCatalogs.generate112(seed).sets();

			for (int index = 0; index < withCopper.size(); index++)
			{
				Map<String, String> expected = GoldenDetails.of(of112.get(index));
				Map<String, String> actual = GoldenDetails.of(withCopper.get(index));

				if (withCopper.get(index).hasRole(VanillaRole.COPPER))
				{
					COPPER_DETAILS.forEach(expected::remove);
					COPPER_DETAILS.forEach(actual::remove);
				}

				assertEquals(expected, actual, "seed " + seed + ", set " + index);
			}
		}
	}
}
