package fr.minecraftpp.core.set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.ore.Color;
import fr.minecraftpp.core.ore.FlammabilityOf;
import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.ore.Rarity;

class OreSetDefinitionTest
{
	@Test
	void acceptsValidValues()
	{
		assertDoesNotThrow(() -> simpleSet("xyzium", Optional.empty()));
	}

	@Test
	void rejectsALightLevelAboveFifteen()
	{
		assertThrows(IllegalArgumentException.class, () -> block(16, 255));
	}

	@Test
	void rejectsAnOpacityAbove255()
	{
		assertThrows(IllegalArgumentException.class, () -> block(0, 256));
	}

	@Test
	void rejectsUnknownTextures()
	{
		assertThrows(IllegalArgumentException.class, () -> new ItemTraits(0, Color.WHITE, false, 0, false, false, false, Optional.empty()));
		assertThrows(IllegalArgumentException.class, () -> new OreTraits(5, HarvestLevel.STONE, new OreDrop.Itself(), false));
		assertThrows(IllegalArgumentException.class, () -> new MaterialDefinition(0, 3, HarvestLevel.STONE, 10));
	}

	@Test
	void rejectsInvalidDrops()
	{
		assertThrows(IllegalArgumentException.class, () -> new OreDrop.Items(3, 2, 0, 1));
		assertThrows(IllegalArgumentException.class, () -> new OreDrop.Items(1, 1, 3, 2));
	}

	@Test
	void rejectsNamesThatAreNotLowercaseLetters()
	{
		assertThrows(IllegalArgumentException.class, () -> simpleSet("Xyzium", Optional.empty()));
		assertThrows(IllegalArgumentException.class, () -> simpleSet("xyz ium", Optional.empty()));
	}

	@Test
	void requiresAMaterialOnlyForMaterialSets()
	{
		assertThrows(IllegalArgumentException.class, () -> simpleSet("xyzium", Optional.of(new MaterialDefinition(1, 1, HarvestLevel.IRON, 10))));
	}

	@Test
	void rolesCannotBeModified()
	{
		Set<VanillaRole> roles = simpleSet("xyzium", Optional.empty()).roles();

		assertThrows(UnsupportedOperationException.class, () -> roles.add(VanillaRole.IRON));
		assertTrue(roles.contains(VanillaRole.COAL));
	}

	private static OreSetDefinition simpleSet(String name, Optional<MaterialDefinition> material)
	{
		ItemTraits item = new ItemTraits(1, new Color(10, 20, 30), false, 0, false, false, false, Optional.empty());
		OreTraits ore = new OreTraits(1, HarvestLevel.WOOD, new OreDrop.Items(1, 1, 0, 2), false);

		return new OreSetDefinition(0, name, SetType.SIMPLE, Rarity.COMMON, EnumSet.of(VanillaRole.COAL), new OreGeneration(1, 5, 128), item, block(0, 255), ore, material);
	}

	private static StorageBlockTraits block(int lightLevel, int lightOpacity)
	{
		return new StorageBlockTraits(1, HarvestLevel.WOOD, false, false, 0, FlammabilityOf.STONE, 1, false, 0, 0, lightLevel, lightOpacity, StorageBlockTraits.DEFAULT_SLIPPERINESS);
	}
}
