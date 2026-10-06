package fr.minecraftpp.content.material;

import fr.minecraftpp.content.item.DynamicAxeItem;
import fr.minecraftpp.content.item.DynamicHoeItem;
import fr.minecraftpp.content.item.DynamicItem;
import fr.minecraftpp.content.item.DynamicShovelItem;
import fr.minecraftpp.core.ore.Rarity;
import fr.minecraftpp.core.set.material.ToolStats.ToolAttack;
import fr.minecraftpp.core.set.material.ToolType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

/**
 * Creates the five tools of a material. The axe, shovel and hoe use the vanilla classes that strip logs, make paths and till the soil.
 */
public final class ToolItemFactory
{
	private ToolItemFactory()
	{
	}

	public static Item create(ToolType toolType, ToolMaterial material, ToolAttack attack, Rarity rarity, Item.Properties properties)
	{
		return switch (toolType)
		{
			case SWORD -> new DynamicItem(properties.sword(material, attack.damage(), attack.speed()), rarity, false);
			case PICKAXE -> new DynamicItem(properties.pickaxe(material, attack.damage(), attack.speed()), rarity, false);
			case AXE -> new DynamicAxeItem(material, attack.damage(), attack.speed(), properties, rarity);
			case SHOVEL -> new DynamicShovelItem(material, attack.damage(), attack.speed(), properties, rarity);
			case HOE -> new DynamicHoeItem(material, attack.damage(), attack.speed(), properties, rarity);
		};
	}
}
