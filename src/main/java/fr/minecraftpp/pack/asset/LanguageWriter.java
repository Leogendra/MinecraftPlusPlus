package fr.minecraftpp.pack.asset;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.TagIds;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;
import fr.minecraftpp.core.text.DisplayNameFormatter;
import fr.minecraftpp.core.variant.VariantCatalog;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;
import fr.minecraftpp.pack.data.DamageTypeWriter;
import net.minecraft.resources.Identifier;

/**
 * Writes the English names of the generated content, as 1.12 named it: blocks and items, the death messages of the blocks that hurt, and the tags the mod creates. The texts that do not depend on the seed are in the static language file of the mod.
 */
public final class LanguageWriter implements GeneratedResourceWriter
{
	public static final String LANGUAGE_FILE = "lang/en_us.json";

	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		Map<String, String> translations = new LinkedHashMap<>();

		for (OreSetDefinition set : catalog.sets())
		{
			addBlockNames(translations, set);
			addItemNames(translations, set);
			addDeathMessages(translations, set);
		}

		addTagNames(translations, catalog);

		return List.of(GeneratedFile.asset(LANGUAGE_FILE, translations));
	}

	private static void addBlockNames(Map<String, String> translations, OreSetDefinition set)
	{
		translations.put(blockKey(ContentIds.ore(set)), DisplayNameFormatter.oreName(set));
		translations.put(blockKey(ContentIds.deepslateOre(set)), DisplayNameFormatter.deepslateOreName(set));
		translations.put(blockKey(ContentIds.storageBlock(set)), DisplayNameFormatter.storageBlockName(set));
	}

	private static void addItemNames(Map<String, String> translations, OreSetDefinition set)
	{
		translations.put(itemKey(ContentIds.item(set)), DisplayNameFormatter.itemName(set));

		if (set.type() == SetType.METAL)
		{
			translations.put(itemKey(ContentIds.nugget(set)), DisplayNameFormatter.nuggetName(set));
		}

		if (set.material().isPresent())
		{
			Arrays.stream(ToolType.values()).forEach(toolType -> translations.put(itemKey(ContentIds.tool(set, toolType)), DisplayNameFormatter.toolName(set, toolType)));
			Arrays.stream(ArmorPiece.values()).forEach(piece -> translations.put(itemKey(ContentIds.armor(set, piece)), DisplayNameFormatter.armorName(set, piece)));
		}
	}

	private static void addDeathMessages(Map<String, String> translations, OreSetDefinition set)
	{
		if (set.block().walkDamage() > 0)
		{
			translations.put("death.attack." + DamageTypeWriter.messageId(set), DisplayNameFormatter.walkDamageDeathMessage(set));
			translations.put("death.attack." + DamageTypeWriter.messageId(set) + ".player", DisplayNameFormatter.walkDamageDeathMessageWhileFighting(set));
		}
	}

	/**
	 * The repair tags and the variant tags of the vanilla items. The variant tags of vanilla tags depend on the vanilla recipes and keep their identifier as name.
	 */
	private static void addTagNames(Map<String, String> translations, OreCatalog catalog)
	{
		for (OreSetDefinition set : catalog.sets())
		{
			if (set.material().isPresent())
			{
				translations.put(itemTagKey(ContentIds.full(TagIds.repairMaterials(set))), DisplayNameFormatter.repairMaterialsName(set));
			}
		}

		for (String vanillaItem : VariantCatalog.of(catalog).vanillaItems())
		{
			translations.put(itemTagKey(TagIds.variantsOf(vanillaItem)), titleCase(Identifier.parse(vanillaItem).getPath()) + " Variants");
		}
	}

	private static String blockKey(String blockId)
	{
		return "block." + ContentIds.NAMESPACE + "." + blockId;
	}

	private static String itemKey(String itemId)
	{
		return "item." + ContentIds.NAMESPACE + "." + itemId;
	}

	/**
	 * The key under which the Fabric tag conventions look for the name of an item tag.
	 */
	private static String itemTagKey(String tagId)
	{
		Identifier tag = Identifier.parse(tagId);

		return "tag.item." + tag.getNamespace() + "." + tag.getPath().replace('/', '.');
	}

	private static String titleCase(String path)
	{
		return Arrays.stream(path.split("_")).map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1)).collect(Collectors.joining(" "));
	}
}
