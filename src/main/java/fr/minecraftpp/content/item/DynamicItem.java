package fr.minecraftpp.content.item;

import fr.minecraftpp.core.ore.Rarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

/**
 * An item of a generated set: its main item, its nugget, its tools and armor. Its name has the color of the set rarity, and the main item can light fires.
 */
public class DynamicItem extends Item
{
	private final Rarity rarity;
	private final boolean firestarter;

	public DynamicItem(Item.Properties properties, Rarity rarity, boolean firestarter)
	{
		super(properties);
		this.rarity = rarity;
		this.firestarter = firestarter;
	}

	@Override
	public Component getName(ItemStack itemStack)
	{
		return RarityNames.colored(super.getName(itemStack), this.rarity);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if (this.firestarter)
		{
			return FireStarter.lightFire(context);
		}
		else
		{
			return super.useOn(context);
		}
	}
}
