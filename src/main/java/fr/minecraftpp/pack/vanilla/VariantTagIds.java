package fr.minecraftpp.pack.vanilla;

import fr.minecraftpp.core.set.ContentIds;
import net.minecraft.resources.Identifier;

/**
 * Naming rule of the variant tags: {@code minecraftpp:variants/iron_ingot} holds the vanilla iron ingot and its generated variants, {@code minecraftpp:variants/tag/coals} the vanilla tag {@code minecraft:coals} and the variants of its items.
 */
public final class VariantTagIds
{
	private VariantTagIds()
	{
	}

	/**
	 * @param vanillaItemId a complete vanilla item identifier, such as {@code minecraft:iron_ingot}
	 */
	public static String ofItem(String vanillaItemId)
	{
		return ContentIds.full("variants/" + Identifier.parse(vanillaItemId).getPath());
	}

	/**
	 * @param vanillaTagId a complete vanilla tag identifier without {@code #}, such as {@code minecraft:coals}
	 */
	public static String ofTag(String vanillaTagId)
	{
		return ContentIds.full("variants/tag/" + Identifier.parse(vanillaTagId).getPath());
	}
}
