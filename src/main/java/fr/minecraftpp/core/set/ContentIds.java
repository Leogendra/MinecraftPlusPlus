package fr.minecraftpp.core.set;

import java.util.List;
import java.util.Locale;

import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;

/**
 * Naming rule of the generated identifiers, without namespace: {@code xyzium_ore}, {@code xyzium_block}, {@code xyzium_pickaxe}...
 *
 * The main item is named after the set ({@code xyzium}) except in metal sets, where it is an ingot ({@code xyzium_ingot}), as in 1.12.
 */
public final class ContentIds
{
	public static final String NAMESPACE = "minecraftpp";

	private ContentIds()
	{
	}

	/**
	 * The complete identifier of a generated content, in the mod namespace.
	 */
	public static String full(String path)
	{
		return NAMESPACE + ":" + path;
	}

	public static String item(OreSetDefinition set)
	{
		if (set.type() == SetType.METAL)
		{
			return set.name() + "_ingot";
		}
		else
		{
			return set.name();
		}
	}

	public static String storageBlock(OreSetDefinition set)
	{
		return set.name() + "_block";
	}

	public static String ore(OreSetDefinition set)
	{
		return set.name() + "_ore";
	}

	public static String deepslateOre(OreSetDefinition set)
	{
		return "deepslate_" + set.name() + "_ore";
	}

	/**
	 * The three blocks of a set: its ore, the deepslate variant of the ore and its storage block.
	 */
	public static List<String> blocks(OreSetDefinition set)
	{
		return List.of(ore(set), deepslateOre(set), storageBlock(set));
	}

	/**
	 * The damage type of a block that hurts the entities walking on it, named after the block.
	 */
	public static String walkDamage(OreSetDefinition set)
	{
		return storageBlock(set);
	}

	public static String nugget(OreSetDefinition set)
	{
		return set.name() + "_nugget";
	}

	public static String tool(OreSetDefinition set, ToolType toolType)
	{
		return set.name() + "_" + toolType.name().toLowerCase(Locale.ROOT);
	}

	public static String armor(OreSetDefinition set, ArmorPiece piece)
	{
		return set.name() + "_" + piece.name().toLowerCase(Locale.ROOT);
	}

	/**
	 * Identifier shared by the tool material, the armor material and their tags.
	 */
	public static String material(OreSetDefinition set)
	{
		return set.name();
	}
}
