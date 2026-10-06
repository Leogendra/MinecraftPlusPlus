package fr.minecraftpp.content;

import java.util.function.Function;

import fr.minecraftpp.content.block.BlockPropertiesFactory;
import fr.minecraftpp.content.block.DynamicBlock;
import fr.minecraftpp.content.block.DynamicOreBlock;
import fr.minecraftpp.content.block.behaviour.BlockBehaviourModules;
import fr.minecraftpp.content.item.DynamicBlockItem;
import fr.minecraftpp.content.item.DynamicItem;
import fr.minecraftpp.content.item.ItemPropertiesFactory;
import fr.minecraftpp.content.material.ArmorItemFactory;
import fr.minecraftpp.content.material.MaterialFactory;
import fr.minecraftpp.content.material.ToolItemFactory;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.MaterialDefinition;
import fr.minecraftpp.core.set.material.ToolType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.level.Level;
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
			registerItems(content, set);
		}

		return content;
	}

	/**
	 * Each set has an ore, its deepslate variant (decision D6) and a storage block, each with its block item.
	 */
	private static void registerBlocks(RegisteredContent content, OreSetDefinition set)
	{
		BlockBehaviourModules oreModules = BlockBehaviourModules.forOre(set.ore());
		BlockBehaviourModules storageModules = BlockBehaviourModules.forStorageBlock(set.block(), walkDamageSource(set));

		registerBlockWithItem(content, set, ContentIds.ore(set), properties -> new DynamicOreBlock(properties, set.ore(), oreModules), BlockPropertiesFactory.ore());
		registerBlockWithItem(content, set, ContentIds.deepslateOre(set), properties -> new DynamicOreBlock(properties, set.ore(), oreModules), BlockPropertiesFactory.deepslateOre());
		registerBlockWithItem(content, set, ContentIds.storageBlock(set), properties -> new DynamicBlock(properties, set.block(), storageModules), BlockPropertiesFactory.storageBlock(set.block()));
	}

	/**
	 * The main item of each set, and the nugget of the metal sets.
	 */
	private static void registerItems(RegisteredContent content, OreSetDefinition set)
	{
		registerItem(content, ContentIds.item(set), properties -> new DynamicItem(properties, set.rarity(), set.item().firestarter()), ItemPropertiesFactory.mainItem(set.item()));

		if (set.type() == SetType.METAL)
		{
			registerItem(content, ContentIds.nugget(set), properties -> new DynamicItem(properties, set.rarity(), false), new Item.Properties());
		}

		set.material().ifPresent(material -> registerEquipment(content, set, material));
	}

	/**
	 * The five tools and four armor pieces of a material or metal set.
	 */
	private static void registerEquipment(RegisteredContent content, OreSetDefinition set, MaterialDefinition material)
	{
		ToolMaterial toolMaterial = MaterialFactory.toolMaterial(set, material);
		ArmorMaterial armorMaterial = MaterialFactory.armorMaterial(set, material);

		for (ToolType toolType : ToolType.values())
		{
			registerItem(content, ContentIds.tool(set, toolType), properties -> ToolItemFactory.create(toolType, toolMaterial, material.tools().attack(toolType), set.rarity(), properties), new Item.Properties());
		}

		for (ArmorPiece piece : ArmorPiece.values())
		{
			registerItem(content, ContentIds.armor(set, piece), properties -> ArmorItemFactory.create(piece, armorMaterial, set.rarity(), properties), new Item.Properties());
		}
	}

	private static void registerBlockWithItem(RegisteredContent content, OreSetDefinition set, String path, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties)
	{
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, identifier(path));
		Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(properties.setId(blockKey)));
		content.addBlock(path, block);

		registerItem(content, path, itemProperties -> new DynamicBlockItem(block, itemProperties.useBlockDescriptionPrefix(), set.rarity()), new Item.Properties());
	}

	/**
	 * The damage of the set's storage block, of the damage type written in the generated pack (decision D8). The type is read from the level, where the data packs registered it.
	 */
	private static Function<Level, DamageSource> walkDamageSource(OreSetDefinition set)
	{
		ResourceKey<DamageType> damageType = ResourceKey.create(Registries.DAMAGE_TYPE, identifier(ContentIds.walkDamage(set)));

		return level -> new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(damageType));
	}

	static void registerItem(RegisteredContent content, String path, Function<Item.Properties, Item> factory, Item.Properties properties)
	{
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, identifier(path));
		Item item = factory.apply(properties.setId(itemKey));

		if (item instanceof DynamicBlockItem blockItem)
		{
			blockItem.registerBlocks(Item.BY_BLOCK, item);
		}

		content.addItem(path, Registry.register(BuiltInRegistries.ITEM, itemKey, item));
	}

	public static Identifier identifier(String path)
	{
		return Identifier.fromNamespaceAndPath(ContentIds.NAMESPACE, path);
	}
}
