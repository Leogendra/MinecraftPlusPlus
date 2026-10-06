package fr.minecraftpp.content;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

/**
 * Lists the generated content in the creative tabs where the vanilla equivalents are, set by set, in catalog order.
 */
public final class CreativeTabEntries
{
	private CreativeTabEntries()
	{
	}

	/**
	 * The vanilla tabs that receive generated content.
	 */
	public enum Tab
	{
		BUILDING_BLOCKS(CreativeModeTabs.BUILDING_BLOCKS), NATURAL_BLOCKS(CreativeModeTabs.NATURAL_BLOCKS), INGREDIENTS(CreativeModeTabs.INGREDIENTS), TOOLS_AND_UTILITIES(CreativeModeTabs.TOOLS_AND_UTILITIES), COMBAT(CreativeModeTabs.COMBAT);

		private final ResourceKey<CreativeModeTab> key;

		Tab(ResourceKey<CreativeModeTab> key)
		{
			this.key = key;
		}
	}

	public static void register(OreCatalog catalog, RegisteredContent content)
	{
		for (Map.Entry<Tab, List<String>> tab : entries(catalog).entrySet())
		{
			CreativeModeTabEvents.modifyOutputEvent(tab.getKey().key).register(output -> tab.getValue().forEach(path -> output.accept(content.item(path))));
		}
	}

	/**
	 * The identifier paths of the content of each tab, in display order. As in vanilla, the axes are listed both with the tools and with the weapons.
	 */
	public static Map<Tab, List<String>> entries(OreCatalog catalog)
	{
		Map<Tab, List<String>> entries = new EnumMap<>(Tab.class);
		for (Tab tab : Tab.values())
		{
			entries.put(tab, new ArrayList<>());
		}

		for (OreSetDefinition set : catalog.sets())
		{
			entries.get(Tab.NATURAL_BLOCKS).addAll(List.of(ContentIds.ore(set), ContentIds.deepslateOre(set)));
			entries.get(Tab.BUILDING_BLOCKS).add(ContentIds.storageBlock(set));
			entries.get(Tab.INGREDIENTS).add(ContentIds.item(set));

			if (set.type() == SetType.METAL)
			{
				entries.get(Tab.INGREDIENTS).add(ContentIds.nugget(set));
			}

			if (set.type().hasMaterial())
			{
				addEquipment(entries, set);
			}
		}

		return entries;
	}

	private static void addEquipment(Map<Tab, List<String>> entries, OreSetDefinition set)
	{
		for (ToolType toolType : List.of(ToolType.SHOVEL, ToolType.PICKAXE, ToolType.AXE, ToolType.HOE))
		{
			entries.get(Tab.TOOLS_AND_UTILITIES).add(ContentIds.tool(set, toolType));
		}

		entries.get(Tab.COMBAT).add(ContentIds.tool(set, ToolType.SWORD));
		entries.get(Tab.COMBAT).add(ContentIds.tool(set, ToolType.AXE));

		for (ArmorPiece piece : ArmorPiece.values())
		{
			entries.get(Tab.COMBAT).add(ContentIds.armor(set, piece));
		}
	}
}
