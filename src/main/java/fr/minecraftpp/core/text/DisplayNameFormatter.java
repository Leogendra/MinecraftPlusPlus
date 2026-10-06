package fr.minecraftpp.core.text;

import java.util.Locale;

import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;

/**
 * The English display names of the generated content, built like the 1.12 ModLanguage: the set name with a capital letter, followed by the kind of content ("Xyzium Block").
 */
public final class DisplayNameFormatter
{
	private DisplayNameFormatter()
	{
	}

	public static String itemName(OreSetDefinition set)
	{
		if (set.type() == SetType.METAL)
		{
			return setName(set) + " Ingot";
		}
		else
		{
			return setName(set);
		}
	}

	public static String storageBlockName(OreSetDefinition set)
	{
		return setName(set) + " Block";
	}

	public static String oreName(OreSetDefinition set)
	{
		return setName(set) + " Ore";
	}

	public static String deepslateOreName(OreSetDefinition set)
	{
		return "Deepslate " + oreName(set);
	}

	public static String nuggetName(OreSetDefinition set)
	{
		return setName(set) + " Nugget";
	}

	public static String toolName(OreSetDefinition set, ToolType toolType)
	{
		return setName(set) + " " + capitalize(toolType.name().toLowerCase(Locale.ROOT));
	}

	public static String armorName(OreSetDefinition set, ArmorPiece piece)
	{
		return setName(set) + " " + capitalize(piece.name().toLowerCase(Locale.ROOT));
	}

	/**
	 * Death message of an entity killed by walking on the storage block; %1$s is the name of the victim.
	 */
	public static String walkDamageDeathMessage(OreSetDefinition set)
	{
		return "%1$s was killed on " + storageBlockName(set);
	}

	/**
	 * Death message when another entity gets the kill credit; %2$s is the name of that entity. 1.12 had no such message.
	 */
	public static String walkDamageDeathMessageWhileFighting(OreSetDefinition set)
	{
		return walkDamageDeathMessage(set) + " while fighting %2$s";
	}

	/**
	 * Name of the tag of the items that repair the tools and armor of the set.
	 */
	public static String repairMaterialsName(OreSetDefinition set)
	{
		return setName(set) + " Repair Materials";
	}

	public static String setName(OreSetDefinition set)
	{
		return capitalize(set.name());
	}

	private static String capitalize(String word)
	{
		return word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1);
	}
}
