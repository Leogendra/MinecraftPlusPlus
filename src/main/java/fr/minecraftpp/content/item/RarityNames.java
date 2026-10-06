package fr.minecraftpp.content.item;

import fr.minecraftpp.core.ore.Rarity;
import net.minecraft.network.chat.Component;

/**
 * Colors the names of the generated content with the color of their rarity (decision D4). The six levels of the 1.12 version do not fit in the four vanilla rarities, so the color is carried by the name itself.
 */
public final class RarityNames
{
	private RarityNames()
	{
	}

	public static Component colored(Component name, Rarity rarity)
	{
		return name.copy().withColor(rarity.getNameColor());
	}
}
