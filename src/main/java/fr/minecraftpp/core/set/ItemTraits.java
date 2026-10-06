package fr.minecraftpp.core.set;

import java.util.Objects;
import java.util.Optional;

import fr.minecraftpp.core.ore.Color;

/**
 * Traits of the main item of a set (gem, dust or ingot).
 *
 * @param textureId     index of the item_N texture, from 1 to 6 (6 is the ingot texture of metal sets)
 * @param fuelTicks     furnace burn time in ticks, 0 when the item is not a fuel
 * @param firestarter   the item lights a fire when used on a block, like flint and steel
 * @param beaconPayment the item is accepted by the beacon
 */
public record ItemTraits(int textureId, Color color, boolean shiny, int fuelTicks, boolean enchantingCurrency, boolean firestarter, boolean beaconPayment, Optional<FoodDefinition> food)
{
	public static final int TEXTURE_COUNT = 6;

	public ItemTraits
	{
		Objects.requireNonNull(color, "color");
		Objects.requireNonNull(food, "food");
		Bounds.check("item texture", textureId, 1, TEXTURE_COUNT);
		Bounds.check("item fuel ticks", fuelTicks, 0, Integer.MAX_VALUE);
	}
}
