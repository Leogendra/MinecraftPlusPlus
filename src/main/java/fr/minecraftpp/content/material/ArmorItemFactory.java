package fr.minecraftpp.content.material;

import fr.minecraftpp.content.item.DynamicItem;
import fr.minecraftpp.core.ore.Rarity;
import fr.minecraftpp.core.set.material.ArmorPiece;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;

/**
 * Creates the four armor pieces of a material.
 */
public final class ArmorItemFactory
{
	private ArmorItemFactory()
	{
	}

	public static Item create(ArmorPiece piece, ArmorMaterial material, Rarity rarity, Item.Properties properties)
	{
		return new DynamicItem(properties.humanoidArmor(material, MaterialFactory.armorType(piece)), rarity, false);
	}
}
