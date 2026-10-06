package fr.minecraftpp.content;

import java.util.function.Function;

import fr.minecraftpp.content.block.BlockPropertiesFactory;
import fr.minecraftpp.content.block.DynamicBlock;
import fr.minecraftpp.content.block.DynamicOreBlock;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Registers the blocks and items of the generated sets in the game registries, during the mod initialization.
 */
public final class ContentRegistrar
{
	private ContentRegistrar()
	{
	}

	public static RegisteredContent register(OreCatalog catalog)
	{
		RegisteredContent content = new RegisteredContent();

		for (OreSetDefinition set : catalog.sets())
		{
			registerBlocks(content, set);
		}

		return content;
	}

	/**
	 * Each set has an ore, its deepslate variant (decision D6) and a storage block, each with its block item.
	 */
	private static void registerBlocks(RegisteredContent content, OreSetDefinition set)
	{
		registerBlockWithItem(content, ContentIds.ore(set), properties -> new DynamicOreBlock(properties, set.ore()), BlockPropertiesFactory.ore());
		registerBlockWithItem(content, ContentIds.deepslateOre(set), properties -> new DynamicOreBlock(properties, set.ore()), BlockPropertiesFactory.deepslateOre());
		registerBlockWithItem(content, ContentIds.storageBlock(set), properties -> new DynamicBlock(properties, set.block()), BlockPropertiesFactory.storageBlock(set.block()));
	}

	private static void registerBlockWithItem(RegisteredContent content, String path, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties)
	{
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, identifier(path));
		Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(properties.setId(blockKey)));
		content.addBlock(path, block);

		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, identifier(path));
		BlockItem item = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
		item.registerBlocks(Item.BY_BLOCK, item);
		content.addItem(path, Registry.register(BuiltInRegistries.ITEM, itemKey, item));
	}

	public static Identifier identifier(String path)
	{
		return Identifier.fromNamespaceAndPath(ContentIds.NAMESPACE, path);
	}
}
