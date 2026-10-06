package fr.minecraftpp.core.set.generator;

import java.util.HashMap;
import java.util.Map;

import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.variant.VanillaVariant;
import fr.minecraftpp.core.variant.VariantRules;

/**
 * State shared by the sets during the effects pass: the variants registered by the sets already processed, and whether the 1.12 villager trades were already initialized.
 */
public class GenerationContext
{
	private final Map<String, Integer> variantCounts = new HashMap<>();
	private boolean villagerTradesInitialized = false;

	/**
	 * Records the variants a role gives to a set, when the role is applied, as the 1.12 sets did.
	 */
	void registerVariants(SetType type, VanillaRole role)
	{
		for (VanillaVariant variant : VariantRules.variantsOf(type, role))
		{
			this.variantCounts.merge(variant.vanillaItemId(), 1, Integer::sum);
		}
	}

	int variantCount(String vanillaItemId)
	{
		return this.variantCounts.getOrDefault(vanillaItemId, 0);
	}

	/**
	 * Marks the villager trades as initialized, and tells whether this call is the first one.
	 */
	boolean initializeVillagerTrades()
	{
		boolean firstTime = !this.villagerTradesInitialized;
		this.villagerTradesInitialized = true;
		return firstTime;
	}
}
