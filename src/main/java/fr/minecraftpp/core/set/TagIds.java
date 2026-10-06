package fr.minecraftpp.core.set;

/**
 * Naming rule of the generated tags, without namespace. The tags themselves are written in the generated pack.
 */
public final class TagIds
{
	private TagIds()
	{
	}

	/**
	 * The items that repair the tools and armor of a set: its main item.
	 */
	public static String repairMaterials(OreSetDefinition set)
	{
		return set.name() + "_repair_materials";
	}

	/**
	 * The items the enchanting table takes instead of lapis lazuli: the main items of the enchanting currency sets.
	 */
	public static String enchantingCurrency()
	{
		return "enchanting_currency";
	}

	/**
	 * The tag of a vanilla item or block and its generated variants: {@code minecraftpp:variants/iron_ingot} for {@code minecraft:iron_ingot}. The rewritten vanilla data and the mixins accept this tag wherever the vanilla object was accepted.
	 *
	 * @param vanillaId a complete vanilla identifier, such as {@code minecraft:iron_ingot}
	 */
	public static String variantsOf(String vanillaId)
	{
		return ContentIds.full("variants/" + pathOf(vanillaId));
	}

	/**
	 * The tag of a vanilla item tag and the variants of its items: {@code minecraftpp:variants/tag/coals} for {@code minecraft:coals}.
	 *
	 * @param vanillaTagId a complete vanilla tag identifier without {@code #}
	 */
	public static String variantsOfTag(String vanillaTagId)
	{
		return ContentIds.full("variants/tag/" + pathOf(vanillaTagId));
	}

	private static String pathOf(String identifier)
	{
		return identifier.substring(identifier.indexOf(':') + 1);
	}
}
