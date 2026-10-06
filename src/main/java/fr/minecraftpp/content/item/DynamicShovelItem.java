package fr.minecraftpp.content.item;

import fr.minecraftpp.core.ore.Rarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;

/**
 * A generated shovel: it makes paths like the vanilla shovels, and its name has the color of the set rarity.
 */
public class DynamicShovelItem extends ShovelItem
{
	private final Rarity rarity;

	public DynamicShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties, Rarity rarity)
	{
		super(material, attackDamage, attackSpeed, properties);
		this.rarity = rarity;
	}

	@Override
	public Component getName(ItemStack itemStack)
	{
		return RarityNames.colored(super.getName(itemStack), this.rarity);
	}
}
