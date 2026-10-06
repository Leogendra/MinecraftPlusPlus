package fr.minecraftpp.gametest;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreSetDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * The sets of the game test seed -7046029254386353131 (see writeGameTestSeed in build.gradle):
 * <ul>
 * <li>coms: metal, beacon, fuel, gold;</li>
 * <li>voging: simple, beacon, coal, edible, slippery, walk damage;</li>
 * <li>brumed: material, blue dye, beacon, diamond;</li>
 * <li>formutlest: simple, currency, slippery;</li>
 * <li>novic: simple, redstone, beacon, firestarter;</li>
 * <li>imer: simple, enchanting currency, copper;</li>
 * <li>ing: metal, iron, shiny, accelerating.</li>
 * </ul>
 */
public final class GameTestSets
{
	private GameTestSets()
	{
	}

	public static OreSetDefinition set(String name)
	{
		return MinecraftPlusPlus.catalog().sets().stream().filter(candidate -> candidate.name().equals(name)).findFirst().orElseThrow(() -> new IllegalStateException("The game tests expect seed -7046029254386353131, which has a set " + name));
	}

	public static Block storageBlock(String name)
	{
		return MinecraftPlusPlus.content().block(ContentIds.storageBlock(set(name)));
	}

	public static Block ore(String name)
	{
		return MinecraftPlusPlus.content().block(ContentIds.ore(set(name)));
	}

	public static Item item(String name)
	{
		return MinecraftPlusPlus.content().item(ContentIds.item(set(name)));
	}
}
