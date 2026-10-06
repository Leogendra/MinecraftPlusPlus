package fr.minecraftpp.core.set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.ore.Color;
import fr.minecraftpp.core.ore.FlammabilityOf;
import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.ore.Rarity;
import fr.minecraftpp.core.ore.ToolType;

class ContentIdsTest
{
	@Test
	void namesTheBlocksAfterTheSet()
	{
		OreSetDefinition set = set(SetType.SIMPLE);

		assertEquals("xyzium_ore", ContentIds.ore(set));
		assertEquals("deepslate_xyzium_ore", ContentIds.deepslateOre(set));
		assertEquals("xyzium_block", ContentIds.storageBlock(set));
	}

	@Test
	void namesTheMainItemAfterTheSetExceptForMetals()
	{
		assertEquals("xyzium", ContentIds.item(set(SetType.MATERIAL)));
		assertEquals("xyzium_ingot", ContentIds.item(set(SetType.METAL)));
	}

	@Test
	void namesToolsArmorAndNuggetsInLowercase()
	{
		OreSetDefinition set = set(SetType.METAL);

		assertEquals("xyzium_pickaxe", ContentIds.tool(set, ToolType.PICKAXE));
		assertEquals("xyzium_shovel", ContentIds.tool(set, ToolType.SHOVEL));
		assertEquals("xyzium_chestplate", ContentIds.armor(set, ArmorPiece.CHESTPLATE));
		assertEquals("xyzium_nugget", ContentIds.nugget(set));
	}

	private static OreSetDefinition set(SetType type)
	{
		ItemTraits item = new ItemTraits(1, Color.WHITE, false, 0, false, false, false, Optional.empty());
		StorageBlockTraits block = new StorageBlockTraits(1, HarvestLevel.WOOD, false, false, 0, FlammabilityOf.STONE, 1, false, 0, 0, 0, 255, 0.6F);
		OreTraits ore = new OreTraits(1, HarvestLevel.WOOD, new OreDrop.Itself(), false);
		Optional<MaterialDefinition> material = type.hasMaterial() ? Optional.of(new MaterialDefinition(1, 1, HarvestLevel.IRON, 10)) : Optional.empty();

		return new OreSetDefinition(0, "xyzium", type, Rarity.COMMON, EnumSet.noneOf(VanillaRole.class), new OreGeneration(1, 5, 128), item, block, ore, material);
	}
}
