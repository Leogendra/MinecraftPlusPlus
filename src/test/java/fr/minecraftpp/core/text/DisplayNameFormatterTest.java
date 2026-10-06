package fr.minecraftpp.core.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.ore.Color;
import fr.minecraftpp.core.ore.FlammabilityOf;
import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.ore.Rarity;
import fr.minecraftpp.core.set.ItemTraits;
import fr.minecraftpp.core.set.OreDrop;
import fr.minecraftpp.core.set.OreGeneration;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.OreTraits;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.StorageBlockTraits;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.SampleMaterials;
import fr.minecraftpp.core.set.material.ToolType;

class DisplayNameFormatterTest
{
	@Test
	void capitalizesTheSetName()
	{
		OreSetDefinition set = set(SetType.MATERIAL);

		assertEquals("Xyzium", DisplayNameFormatter.itemName(set));
		assertEquals("Xyzium Block", DisplayNameFormatter.storageBlockName(set));
		assertEquals("Xyzium Ore", DisplayNameFormatter.oreName(set));
		assertEquals("Deepslate Xyzium Ore", DisplayNameFormatter.deepslateOreName(set));
	}

	@Test
	void namesMetalItemsAsIngots()
	{
		OreSetDefinition set = set(SetType.METAL);

		assertEquals("Xyzium Ingot", DisplayNameFormatter.itemName(set));
		assertEquals("Xyzium Nugget", DisplayNameFormatter.nuggetName(set));
	}

	@Test
	void namesToolsAndArmor()
	{
		OreSetDefinition set = set(SetType.MATERIAL);

		assertEquals("Xyzium Shovel", DisplayNameFormatter.toolName(set, ToolType.SHOVEL));
		assertEquals("Xyzium Leggings", DisplayNameFormatter.armorName(set, ArmorPiece.LEGGINGS));
	}

	@Test
	void namesTheBlockInTheDeathMessage()
	{
		assertEquals("%1$s was killed on Xyzium Block", DisplayNameFormatter.walkDamageDeathMessage(set(SetType.SIMPLE)));
	}

	private static OreSetDefinition set(SetType type)
	{
		ItemTraits item = new ItemTraits(1, Color.WHITE, false, 0, false, false, false, Optional.empty());
		StorageBlockTraits block = new StorageBlockTraits(1, HarvestLevel.STONE, false, false, 0, FlammabilityOf.STONE, 1, false, 0, 0, 0, 255, 0.6F);
		OreTraits ore = new OreTraits(1, HarvestLevel.STONE, new OreDrop.Itself(), false);

		return new OreSetDefinition(0, "xyzium", type, Rarity.COMMON, EnumSet.noneOf(VanillaRole.class), new OreGeneration(1, 5, 128), item, block, ore, type.hasMaterial() ? Optional.of(SampleMaterials.ironLike()) : Optional.empty());
	}
}
