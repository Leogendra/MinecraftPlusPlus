package fr.minecraftpp.client;

import java.util.List;

import fr.minecraftpp.content.RegisteredContent;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.Block;

/**
 * Tints the placed blocks of each set with its color, as 1.12 did: the faces of tint index 0 in the block models. The items carry their own tint in their item definition.
 */
public final class DynamicBlockTints
{
	private DynamicBlockTints()
	{
	}

	public static void register(OreCatalog catalog, RegisteredContent content)
	{
		for (OreSetDefinition set : catalog.sets())
		{
			Block[] blocks = ContentIds.blocks(set).stream().map(content::block).toArray(Block[]::new);

			BlockColorRegistry.register(List.of(BlockTintSources.constant(ARGB.opaque(set.item().color().asInt()))), blocks);
		}
	}
}
