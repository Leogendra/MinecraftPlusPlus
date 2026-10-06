package fr.minecraftpp.content;

import fr.minecraftpp.core.set.TagIds;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * The tags of the mod that its code reads; the generated pack fills them.
 */
public final class ModTags
{
	public static final TagKey<Item> ENCHANTING_CURRENCY = TagKey.create(Registries.ITEM, ContentRegistrar.identifier(TagIds.enchantingCurrency()));

	private ModTags()
	{
	}
}
