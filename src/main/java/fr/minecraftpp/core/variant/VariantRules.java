package fr.minecraftpp.core.variant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.minecraftpp.core.set.ContentSlot;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;

/**
 * Which vanilla items the content of a set replaces, according to its roles. The order is the 1.12 registration order: item, block, the five tools, the four armor pieces, then the nugget.
 */
public final class VariantRules
{
	private VariantRules()
	{
	}

	/**
	 * The variants a role gives to a set of the given type. Only the coal, iron, gold, diamond and copper roles replace vanilla items; the diamond role has no nugget.
	 */
	public static List<VanillaVariant> variantsOf(SetType type, VanillaRole role)
	{
		return switch (role)
		{
			case COAL -> List.of(item("minecraft:coal"), block("minecraft:coal_block"));
			case IRON -> metalVariants(type, "iron_ingot", "iron_block", "iron", "iron_nugget");
			case GOLD -> metalVariants(type, "gold_ingot", "gold_block", "golden", "gold_nugget");
			case DIAMOND -> metalVariants(type, "diamond", "diamond_block", "diamond", null);
			case COPPER -> metalVariants(type, "copper_ingot", "copper_block", "copper", "copper_nugget");
			default -> List.of();
		};
	}

	private static List<VanillaVariant> metalVariants(SetType type, String ingot, String block, String equipmentPrefix, String nugget)
	{
		List<VanillaVariant> variants = new ArrayList<>();
		variants.add(item("minecraft:" + ingot));
		variants.add(block("minecraft:" + block));

		if (type.hasMaterial())
		{
			for (ToolType toolType : ToolType.values())
			{
				variants.add(new VanillaVariant("minecraft:" + equipmentPrefix + "_" + toolType.name().toLowerCase(Locale.ROOT), new ContentSlot.Tool(toolType)));
			}

			for (ArmorPiece piece : ArmorPiece.values())
			{
				variants.add(new VanillaVariant("minecraft:" + equipmentPrefix + "_" + piece.name().toLowerCase(Locale.ROOT), new ContentSlot.Armor(piece)));
			}
		}

		if (type == SetType.METAL && nugget != null)
		{
			variants.add(new VanillaVariant("minecraft:" + nugget, new ContentSlot.Nugget()));
		}

		return variants;
	}

	private static VanillaVariant item(String vanillaItemId)
	{
		return new VanillaVariant(vanillaItemId, new ContentSlot.MainItem());
	}

	private static VanillaVariant block(String vanillaItemId)
	{
		return new VanillaVariant(vanillaItemId, new ContentSlot.StorageBlock());
	}
}
