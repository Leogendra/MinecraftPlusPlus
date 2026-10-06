package fr.minecraftpp.core.recipe;

import java.util.List;

import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;

/**
 * The crafting patterns of the generated content, the same as the vanilla ones. {@code M} is the material of the set, {@code S} a stick.
 */
public final class RecipePatterns
{
	public static final char MATERIAL = 'M';
	public static final char STICK = 'S';

	/**
	 * Nine items into a block, or nine nuggets into an ingot.
	 */
	public static final List<String> COMPACT = List.of("MMM", "MMM", "MMM");

	private RecipePatterns()
	{
	}

	public static List<String> tool(ToolType toolType)
	{
		return switch (toolType)
		{
			case SWORD -> List.of("M", "M", "S");
			case PICKAXE -> List.of("MMM", " S ", " S ");
			case AXE -> List.of("MM", "MS", " S");
			case SHOVEL -> List.of("M", "S", "S");
			case HOE -> List.of("MM", " S", " S");
		};
	}

	public static List<String> armor(ArmorPiece piece)
	{
		return switch (piece)
		{
			case HELMET -> List.of("MMM", "M M");
			case CHESTPLATE -> List.of("M M", "MMM", "MMM");
			case LEGGINGS -> List.of("MMM", "M M", "M M");
			case BOOTS -> List.of("M M", "M M");
		};
	}
}
