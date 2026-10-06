package fr.minecraftpp.content.item;

import fr.minecraftpp.core.ore.Rarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

/**
 * A generated hoe: it tills the soil like the vanilla hoes, and its name has the color of the set rarity.
 */
public class DynamicHoeItem extends HoeItem
{
	private final Rarity rarity;

	public DynamicHoeItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties, Rarity rarity)
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
