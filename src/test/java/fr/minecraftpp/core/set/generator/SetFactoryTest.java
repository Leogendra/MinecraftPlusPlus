package fr.minecraftpp.core.set.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.ore.OreProperties;
import fr.minecraftpp.core.set.OreDrop;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.trait.TraitCatalog;

class SetFactoryTest
{
	@Test
	void choosesTheKindOfSetFromTheProperties()
	{
		assertEquals(SetType.SIMPLE, generate(List.of(OreProperties.COAL)).type());
		assertEquals(SetType.MATERIAL, generate(List.of(OreProperties.MATERIAL, OreProperties.IRON)).type());
		assertEquals(SetType.METAL, generate(List.of(OreProperties.METAL, OreProperties.GOLD)).type());
	}

	@Test
	void materialSetsIgnoreTheCurrencyRole()
	{
		OreSetDefinition set = generate(List.of(OreProperties.MATERIAL, OreProperties.CURRENCY, OreProperties.BEACON));

		assertFalse(set.hasRole(VanillaRole.CURRENCY));
		assertTrue(set.hasRole(VanillaRole.BEACON));
	}

	@Test
	void metalSetsIgnoreTheBlueDyeAndRedstoneRoles()
	{
		OreSetDefinition set = generate(List.of(OreProperties.METAL, OreProperties.BLUEDYE, OreProperties.REDSTONE, OreProperties.FUEL));

		assertFalse(set.hasRole(VanillaRole.BLUE_DYE));
		assertFalse(set.hasRole(VanillaRole.REDSTONE));
		assertTrue(set.hasRole(VanillaRole.FUEL));
	}

	@Test
	void metalOresDropThemselvesAndGemOresDropItems()
	{
		assertInstanceOf(OreDrop.Itself.class, generate(List.of(OreProperties.METAL)).ore().drop());
		assertInstanceOf(OreDrop.Items.class, generate(List.of(OreProperties.MATERIAL)).ore().drop());
	}

	@Test
	void rolesSetTheHarvestLevels()
	{
		assertEquals(HarvestLevel.WOOD, generate(List.of(OreProperties.COAL)).block().harvestLevel());
		assertEquals(HarvestLevel.IRON, generate(List.of(OreProperties.METAL, OreProperties.DIAMOND)).ore().harvestLevel());
		assertEquals(HarvestLevel.DIAMOND, generate(List.of(OreProperties.METAL, OreProperties.DIAMOND)).material().orElseThrow().miningLevel());
	}

	private static OreSetDefinition generate(List<OreProperties> properties)
	{
		Random rand = new Random(1);
		OreSetGenerator generator = SetFactory.generateSet(properties, rand, "xyzium");
		generator.setupEffects(rand, TraitCatalog.defaults(), new GenerationContext());

		return generator.toDefinition(0);
	}
}
