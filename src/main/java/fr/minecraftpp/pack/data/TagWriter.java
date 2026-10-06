package fr.minecraftpp.pack.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.minecraftpp.content.ModTags;
import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * Adds the generated blocks and items to the vanilla tags that give them their traits: the pickaxe and the tool level needed to mine them, the beacon, the wolf food and the endless fire; and to the enchanting currency tag of the mod. Only the non-empty tags are written.
 */
public final class TagWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		Map<TagKey<?>, List<String>> tags = new LinkedHashMap<>();

		for (OreSetDefinition set : catalog.sets())
		{
			addHarvestTags(tags, set);
			addTraitTags(tags, set);
		}

		return tags.entrySet().stream().map(tag -> new TagJson(tag.getValue()).toFile(tag.getKey())).toList();
	}

	private static void addHarvestTags(Map<TagKey<?>, List<String>> tags, OreSetDefinition set)
	{
		for (String blockId : ContentIds.blocks(set))
		{
			add(tags, BlockTags.MINEABLE_WITH_PICKAXE, blockId);
		}

		neededToolTag(set.ore().harvestLevel()).ifPresent(tag ->
		{
			add(tags, tag, ContentIds.ore(set));
			add(tags, tag, ContentIds.deepslateOre(set));
		});
		neededToolTag(set.block().harvestLevel()).ifPresent(tag -> add(tags, tag, ContentIds.storageBlock(set)));
	}

	private static void addTraitTags(Map<TagKey<?>, List<String>> tags, OreSetDefinition set)
	{
		if (set.block().beaconBase())
		{
			add(tags, BlockTags.BEACON_BASE_BLOCKS, ContentIds.storageBlock(set));
		}

		if (set.block().flammability().isInfinite())
		{
			add(tags, BlockTags.INFINIBURN_OVERWORLD, ContentIds.storageBlock(set));
		}

		if (set.item().beaconPayment())
		{
			add(tags, ItemTags.BEACON_PAYMENT_ITEMS, ContentIds.item(set));
		}

		if (set.item().enchantingCurrency())
		{
			add(tags, ModTags.ENCHANTING_CURRENCY, ContentIds.item(set));
		}

		if (set.item().food().isPresent() && set.item().food().get().wolfFood())
		{
			add(tags, ItemTags.WOLF_FOOD, ContentIds.item(set));
		}
	}

	/**
	 * A wooden tool mines everything a hand-mined block needs, so the lowest level has no tag.
	 */
	private static Optional<TagKey<Block>> neededToolTag(HarvestLevel level)
	{
		return switch (level)
		{
			case WOOD -> Optional.empty();
			case STONE -> Optional.of(BlockTags.NEEDS_STONE_TOOL);
			case IRON -> Optional.of(BlockTags.NEEDS_IRON_TOOL);
			case DIAMOND -> Optional.of(BlockTags.NEEDS_DIAMOND_TOOL);
		};
	}

	private static void add(Map<TagKey<?>, List<String>> tags, TagKey<?> tag, String contentId)
	{
		tags.computeIfAbsent(tag, key -> new ArrayList<>()).add(ContentIds.full(contentId));
	}
}
