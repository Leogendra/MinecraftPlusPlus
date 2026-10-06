package fr.minecraftpp.pack.vanilla;

import java.util.ArrayList;
import java.util.List;

import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.variant.VariantCatalog;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;
import fr.minecraftpp.pack.data.TagJson;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

/**
 * Writes, for each vanilla item that has generated variants, the item tag of the vanilla item and its variants. The rewritten vanilla data accepts this tag wherever it accepted the vanilla item.
 */
public final class VariantTagWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		VariantCatalog variants = VariantCatalog.of(catalog);

		return variants.vanillaItems().stream().map(vanillaItem -> tag(vanillaItem, variants)).toList();
	}

	private static GeneratedFile tag(String vanillaItem, VariantCatalog variants)
	{
		List<String> values = new ArrayList<>();
		values.add(vanillaItem);
		values.addAll(variants.variantsOf(vanillaItem));

		return new TagJson(values).toFile(TagKey.create(Registries.ITEM, Identifier.parse(VariantTagIds.ofItem(vanillaItem))));
	}
}
