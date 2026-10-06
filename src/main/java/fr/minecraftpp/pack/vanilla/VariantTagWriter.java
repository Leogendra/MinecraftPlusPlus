package fr.minecraftpp.pack.vanilla;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.TagIds;
import fr.minecraftpp.core.variant.VariantCatalog;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;
import fr.minecraftpp.pack.data.TagJson;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/**
 * Writes, for each vanilla item that has generated variants, the item tag of the vanilla item and its variants; and for each vanilla storage block, the block tag of the vanilla block and its generated blocks. The rewritten vanilla data and the mixins accept these tags wherever the vanilla object was accepted.
 */
public final class VariantTagWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		VariantCatalog variants = VariantCatalog.of(catalog);
		Stream<GeneratedFile> itemTags = variants.vanillaItems().stream().map(vanillaItem -> tag(Registries.ITEM, vanillaItem, variants.variantsOf(vanillaItem)));
		Stream<GeneratedFile> blockTags = variants.vanillaBlocks().stream().map(vanillaBlock -> tag(Registries.BLOCK, vanillaBlock, variants.blockVariantsOf(vanillaBlock)));

		return Stream.concat(itemTags, blockTags).toList();
	}

	private static <T> GeneratedFile tag(ResourceKey<? extends Registry<T>> registry, String vanillaId, List<String> variants)
	{
		List<String> values = new ArrayList<>();
		values.add(vanillaId);
		values.addAll(variants);

		return new TagJson(values).toFile(TagKey.create(registry, Identifier.parse(TagIds.variantsOf(vanillaId))));
	}
}
