package fr.minecraftpp.pack.asset;

import java.util.Locale;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;

/**
 * Naming rule of the textures shipped in the mod jar, under {@code assets/minecraftpp/textures/}. The textures are grayscale and shared by all the sets: each set tints them with its color.
 */
public final class TextureIds
{
	public static final String STONE = "minecraft:block/stone";
	public static final String DEEPSLATE = "minecraft:block/deepslate";
	public static final String NUGGET = ContentIds.full("item/nugget");

	private TextureIds()
	{
	}

	public static String storageBlock(OreSetDefinition set)
	{
		return ContentIds.full("block/block_" + set.block().textureId());
	}

	/**
	 * The ore texture is an overlay, drawn over stone or deepslate.
	 */
	public static String oreOverlay(OreSetDefinition set)
	{
		return ContentIds.full("block/ore_" + set.ore().textureId());
	}

	public static String mainItem(OreSetDefinition set)
	{
		return ContentIds.full("item/item_" + set.item().textureId());
	}

	public static String tool(ToolType toolType)
	{
		return ContentIds.full("item/" + toolType.name().toLowerCase(Locale.ROOT));
	}

	public static String armor(ArmorPiece piece)
	{
		return ContentIds.full("item/" + piece.name().toLowerCase(Locale.ROOT));
	}
}
