package fr.minecraftpp.content.material;

import java.util.EnumMap;
import java.util.Map;

import fr.minecraftpp.content.ContentRegistrar;
import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.TagIds;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.MaterialDefinition;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

/**
 * Builds the vanilla tool and armor materials of a generated set.
 */
public final class MaterialFactory
{
	private MaterialFactory()
	{
	}

	/**
	 * The attack damage bonus is 0: each tool receives its complete attack, computed in the core as in 1.12. The tools cannot mine the blocks the vanilla tools of the same level cannot mine.
	 */
	public static ToolMaterial toolMaterial(OreSetDefinition set, MaterialDefinition material)
	{
		return new ToolMaterial(incorrectBlocksFor(material.miningLevel()), material.tools().durability(), material.tools().efficiency(), 0.0F, material.enchantability(), repairMaterials(set));
	}

	public static ArmorMaterial armorMaterial(OreSetDefinition set, MaterialDefinition material)
	{
		Map<ArmorType, Integer> defense = new EnumMap<>(ArmorType.class);

		for (ArmorPiece piece : ArmorPiece.values())
		{
			defense.put(armorType(piece), material.armor().defense(piece));
		}

		return new ArmorMaterial(material.armor().durabilityFactor(), defense, material.enchantability(), SoundEvents.ARMOR_EQUIP_GENERIC, material.armor().toughness(), 0.0F, repairMaterials(set), equipmentAsset(set));
	}

	public static ArmorType armorType(ArmorPiece piece)
	{
		return switch (piece)
		{
			case HELMET -> ArmorType.HELMET;
			case CHESTPLATE -> ArmorType.CHESTPLATE;
			case LEGGINGS -> ArmorType.LEGGINGS;
			case BOOTS -> ArmorType.BOOTS;
		};
	}

	/**
	 * The equipment asset, written in the generated pack, that draws the armor on the player.
	 */
	public static ResourceKey<EquipmentAsset> equipmentAsset(OreSetDefinition set)
	{
		return ResourceKey.create(EquipmentAssets.ROOT_ID, ContentRegistrar.identifier(ContentIds.material(set)));
	}

	/**
	 * The tag of the items that repair the tools and armor of the set, written in the generated pack.
	 */
	public static TagKey<Item> repairMaterials(OreSetDefinition set)
	{
		return TagKey.create(Registries.ITEM, ContentRegistrar.identifier(TagIds.repairMaterials(set)));
	}

	private static TagKey<Block> incorrectBlocksFor(HarvestLevel miningLevel)
	{
		return switch (miningLevel)
		{
			case WOOD -> BlockTags.INCORRECT_FOR_WOODEN_TOOL;
			case STONE -> BlockTags.INCORRECT_FOR_STONE_TOOL;
			case IRON -> BlockTags.INCORRECT_FOR_IRON_TOOL;
			case DIAMOND -> BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
		};
	}
}
