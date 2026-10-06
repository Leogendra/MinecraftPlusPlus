package fr.minecraftpp.core.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.ContentSlot;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.VanillaRole;

/**
 * The generated variants of each vanilla item: wherever the game accepts the vanilla item, it also accepts its variants.
 *
 * Identifiers are complete ({@code minecraft:iron_ingot}, {@code minecraftpp:xyzium_ingot}). The variants of an item are listed in registration order, set by set.
 */
public class VariantCatalog
{
	private final Map<String, List<String>> variants;
	private final Map<String, List<String>> blockVariants;

	private VariantCatalog(Map<String, List<String>> variants, Map<String, List<String>> blockVariants)
	{
		this.variants = variants;
		this.blockVariants = blockVariants;
	}

	public static VariantCatalog of(OreCatalog catalog)
	{
		Map<String, List<String>> variants = new LinkedHashMap<>();
		Map<String, List<String>> blockVariants = new LinkedHashMap<>();

		for (OreSetDefinition set : catalog.sets())
		{
			for (VanillaRole role : set.roles())
			{
				for (VanillaVariant variant : VariantRules.variantsOf(set.type(), role))
				{
					String generatedId = ContentIds.full(variant.slot().idFor(set));
					variants.computeIfAbsent(variant.vanillaItemId(), vanillaItemId -> new ArrayList<>()).add(generatedId);

					if (variant.slot() instanceof ContentSlot.StorageBlock)
					{
						blockVariants.computeIfAbsent(variant.vanillaItemId(), vanillaBlockId -> new ArrayList<>()).add(generatedId);
					}
				}
			}
		}

		return new VariantCatalog(immutable(variants), immutable(blockVariants));
	}

	/**
	 * The generated variants of a vanilla item, empty when it has none.
	 */
	public List<String> variantsOf(String vanillaItemId)
	{
		return this.variants.getOrDefault(vanillaItemId, List.of());
	}

	public boolean hasVariants(String vanillaItemId)
	{
		return this.variants.containsKey(vanillaItemId);
	}

	/**
	 * The vanilla items that have at least one variant.
	 */
	public Set<String> vanillaItems()
	{
		return this.variants.keySet();
	}

	/**
	 * The vanilla blocks whose block item has variants that are blocks too, the storage blocks: the iron block and the generated iron blocks. A block and its item share their identifier.
	 */
	public Set<String> vanillaBlocks()
	{
		return this.blockVariants.keySet();
	}

	public List<String> blockVariantsOf(String vanillaBlockId)
	{
		return this.blockVariants.getOrDefault(vanillaBlockId, List.of());
	}

	private static Map<String, List<String>> immutable(Map<String, List<String>> variants)
	{
		variants.replaceAll((vanillaId, generated) -> List.copyOf(generated));
		return Collections.unmodifiableMap(variants);
	}
}
