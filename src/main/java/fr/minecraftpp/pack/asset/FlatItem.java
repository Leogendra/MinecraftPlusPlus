package fr.minecraftpp.pack.asset;

import java.util.ArrayList;
import java.util.List;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;

/**
 * An item drawn from a single texture tinted with the color of its set, as opposed to the block items drawn like their block.
 *
 * @param itemId   the item identifier, without namespace
 * @param handheld the item is held like a tool, as the vanilla tools are
 */
public record FlatItem(String itemId, String texture, boolean handheld)
{
	/**
	 * The main item of the set, the nugget of a metal set, then the tools and armor pieces of an equipped set.
	 */
	public static List<FlatItem> of(OreSetDefinition set)
	{
		List<FlatItem> items = new ArrayList<>();
		items.add(new FlatItem(ContentIds.item(set), TextureIds.mainItem(set), false));

		if (set.type() == SetType.METAL)
		{
			items.add(new FlatItem(ContentIds.nugget(set), TextureIds.NUGGET, false));
		}

		if (set.material().isPresent())
		{
			for (ToolType toolType : ToolType.values())
			{
				items.add(new FlatItem(ContentIds.tool(set, toolType), TextureIds.tool(toolType), true));
			}

			for (ArmorPiece piece : ArmorPiece.values())
			{
				items.add(new FlatItem(ContentIds.armor(set, piece), TextureIds.armor(piece), false));
			}
		}

		return items;
	}
}
