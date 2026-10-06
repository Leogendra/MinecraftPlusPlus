package fr.minecraftpp.core.set;

import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;

/**
 * One kind of content generated for a set: its item, its storage block, its nugget, one of its tools or armor pieces.
 */
public sealed interface ContentSlot permits ContentSlot.MainItem, ContentSlot.StorageBlock, ContentSlot.Nugget, ContentSlot.Tool, ContentSlot.Armor
{
	/**
	 * The identifier of this content for a set, without namespace.
	 */
	String idFor(OreSetDefinition set);

	record MainItem() implements ContentSlot
	{
		@Override
		public String idFor(OreSetDefinition set)
		{
			return ContentIds.item(set);
		}
	}

	record StorageBlock() implements ContentSlot
	{
		@Override
		public String idFor(OreSetDefinition set)
		{
			return ContentIds.storageBlock(set);
		}
	}

	record Nugget() implements ContentSlot
	{
		@Override
		public String idFor(OreSetDefinition set)
		{
			return ContentIds.nugget(set);
		}
	}

	record Tool(ToolType toolType) implements ContentSlot
	{
		@Override
		public String idFor(OreSetDefinition set)
		{
			return ContentIds.tool(set, this.toolType);
		}
	}

	record Armor(ArmorPiece piece) implements ContentSlot
	{
		@Override
		public String idFor(OreSetDefinition set)
		{
			return ContentIds.armor(set, this.piece);
		}
	}
}
