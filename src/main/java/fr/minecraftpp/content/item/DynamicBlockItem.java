package fr.minecraftpp.content.item;

import fr.minecraftpp.core.ore.Rarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * The item of a generated ore or storage block, named with the color of the set rarity.
 */
public class DynamicBlockItem extends BlockItem
{
	private final Rarity rarity;

	public DynamicBlockItem(Block block, Item.Properties properties, Rarity rarity)
	{
		super(block, properties);
		this.rarity = rarity;
	}

	@Override
	public Component getName(ItemStack itemStack)
	{
		return RarityNames.colored(super.getName(itemStack), this.rarity);
	}
}
