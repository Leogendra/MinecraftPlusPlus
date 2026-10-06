package fr.minecraftpp.core.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.ore.Color;
import fr.minecraftpp.core.ore.FlammabilityOf;
import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.ore.Rarity;
import fr.minecraftpp.core.set.FoodDefinition;
import fr.minecraftpp.core.set.ItemTraits;
import fr.minecraftpp.core.set.OreDrop;
import fr.minecraftpp.core.set.OreGeneration;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.OreTraits;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.StorageBlockTraits;
import fr.minecraftpp.core.set.VanillaRole;

class SetInfoFormatterTest
{
	@Test
	void formatsASetWithoutTraits()
	{
		OreSetDefinition set = set(EnumSet.of(VanillaRole.CURRENCY), Optional.empty(), 0, StorageBlockTraits.OPAQUE);

		assertEquals("xyzium : Familiar Simple; currency; ; [level: 64, frequency: 3-7], needs stone to harvest", SetInfoFormatter.formatSet(set));
	}

	@Test
	void coalHidesTheFuelRole()
	{
		OreSetDefinition set = set(EnumSet.of(VanillaRole.COAL, VanillaRole.FUEL, VanillaRole.BEACON), Optional.empty(), 0, StorageBlockTraits.OPAQUE);

		assertEquals("xyzium : Familiar Simple; beacon, coal; ; [level: 64, frequency: 3-7], needs stone to harvest", SetInfoFormatter.formatSet(set));
	}

	@Test
	void listsTheTraitsLikeThe112Command()
	{
		OreSetDefinition set = set(EnumSet.noneOf(VanillaRole.class), Optional.of(new FoodDefinition(4, 3.5F, true, false)), 15, 140);

		assertEquals("xyzium : Familiar Simple; ; food{amount=4, saturation=3.5, wolf food}, glow{15}, opacity{55%}; [level: 64, frequency: 3-7], needs stone to harvest", SetInfoFormatter.formatSet(set));
	}

	private static OreSetDefinition set(EnumSet<VanillaRole> roles, Optional<FoodDefinition> food, int lightLevel, int lightOpacity)
	{
		ItemTraits item = new ItemTraits(1, Color.WHITE, false, 0, false, false, false, food);
		StorageBlockTraits block = new StorageBlockTraits(1, HarvestLevel.STONE, false, false, 0, FlammabilityOf.STONE, 1, false, 0, 0, lightLevel, lightOpacity, StorageBlockTraits.DEFAULT_SLIPPERINESS);
		OreTraits ore = new OreTraits(1, HarvestLevel.STONE, new OreDrop.Items(1, 1, 0, 2), false);

		return new OreSetDefinition(0, "xyzium", SetType.SIMPLE, Rarity.FAMILIAR, roles, new OreGeneration(3, 7, 64), item, block, ore, Optional.empty());
	}
}
