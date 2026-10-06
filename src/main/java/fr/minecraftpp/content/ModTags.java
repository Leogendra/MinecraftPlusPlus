package fr.minecraftpp.content;

import fr.minecraftpp.core.set.TagIds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * The tags of the mod that its code reads; the generated pack fills them. A variant tag always holds the vanilla object too.
 */
public final class ModTags
{
	public static final TagKey<Item> ENCHANTING_CURRENCY = TagKey.create(Registries.ITEM, ContentRegistrar.identifier(TagIds.enchantingCurrency()));
	public static final TagKey<Item> IRON_INGOT_VARIANTS = TagKey.create(Registries.ITEM, Identifier.parse(TagIds.variantsOf("minecraft:iron_ingot")));
	public static final TagKey<Item> GOLD_INGOT_VARIANTS = TagKey.create(Registries.ITEM, Identifier.parse(TagIds.variantsOf("minecraft:gold_ingot")));
	public static final TagKey<Block> IRON_BLOCK_VARIANTS = TagKey.create(Registries.BLOCK, Identifier.parse(TagIds.variantsOf("minecraft:iron_block")));

	private ModTags()
	{
	}
}
