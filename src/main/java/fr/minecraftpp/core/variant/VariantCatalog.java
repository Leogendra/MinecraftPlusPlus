package fr.minecraftpp.core.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.minecraftpp.core.set.ContentIds;
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

	private VariantCatalog(Map<String, List<String>> variants)
	{
		this.variants = variants;
	}

	public static VariantCatalog of(OreCatalog catalog)
	{
		Map<String, List<String>> variants = new LinkedHashMap<>();

		for (OreSetDefinition set : catalog.sets())
		{
			for (VanillaRole role : set.roles())
			{
				for (VanillaVariant variant : VariantRules.variantsOf(set.type(), role))
				{
					variants.computeIfAbsent(variant.vanillaItemId(), vanillaItemId -> new ArrayList<>()).add(ContentIds.full(variant.slot().idFor(set)));
				}
			}
		}

		variants.replaceAll((vanillaItemId, generated) -> List.copyOf(generated));
		return new VariantCatalog(Collections.unmodifiableMap(variants));
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
}
