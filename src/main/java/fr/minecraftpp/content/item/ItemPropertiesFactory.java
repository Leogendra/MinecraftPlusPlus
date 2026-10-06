package fr.minecraftpp.content.item;

import fr.minecraftpp.core.set.FoodDefinition;
import fr.minecraftpp.core.set.ItemTraits;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

/**
 * Translates the item traits into item properties and data components.
 */
public final class ItemPropertiesFactory
{
	private ItemPropertiesFactory()
	{
	}

	/**
	 * The main item of a set: edible and shiny according to its traits.
	 */
	public static Item.Properties mainItem(ItemTraits traits)
	{
		Item.Properties properties = new Item.Properties();

		traits.food().ifPresent(food -> properties.food(foodProperties(food)));

		if (traits.shiny())
		{
			properties.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
		}

		return properties;
	}

	/**
	 * In 1.12 the saturation of a food was a modifier: the saturation given is twice the nutrition times the modifier, which the vanilla builder still computes.
	 */
	public static FoodProperties foodProperties(FoodDefinition food)
	{
		FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(food.nutrition()).saturationModifier(food.saturation());

		if (food.alwaysEdible())
		{
			builder.alwaysEdible();
		}

		return builder.build();
	}
}
