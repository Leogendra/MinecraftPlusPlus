package fr.minecraftpp.core.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import fr.minecraftpp.core.ore.FlammabilityOf;
import fr.minecraftpp.core.set.ItemTraits;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreGeneration;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.StorageBlockTraits;
import fr.minecraftpp.core.set.VanillaRole;

/**
 * The text of the /mppinfo command: one line per set, word for word as in 1.12.
 */
public final class SetInfoFormatter
{
	private SetInfoFormatter()
	{
	}

	public static String format(OreCatalog catalog)
	{
		return catalog.sets().stream().map(SetInfoFormatter::formatSet).collect(Collectors.joining("\n"));
	}

	public static String formatSet(OreSetDefinition set)
	{
		return set.name() + " : " + set.rarity().getDisplayName() + " " + set.type().getDisplayName() + "; " + roles(set) + "; " + traits(set) + "; " + harvest(set);
	}

	/**
	 * The roles in the 1.12 display order, then copper, which 1.12 did not have. The coal role implies the fuel role, which is then not repeated.
	 */
	private static String roles(OreSetDefinition set)
	{
		List<String> roles = new ArrayList<>();

		addIf(roles, set.hasRole(VanillaRole.BLUE_DYE), VanillaRole.BLUE_DYE.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.REDSTONE), VanillaRole.REDSTONE.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.CURRENCY), VanillaRole.CURRENCY.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.BEACON), VanillaRole.BEACON.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.ENCHANTING_CURRENCY), VanillaRole.ENCHANTING_CURRENCY.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.COAL), VanillaRole.COAL.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.FUEL) && !set.hasRole(VanillaRole.COAL), VanillaRole.FUEL.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.IRON), VanillaRole.IRON.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.GOLD), VanillaRole.GOLD.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.DIAMOND), VanillaRole.DIAMOND.getInfoName());
		addIf(roles, set.hasRole(VanillaRole.COPPER), VanillaRole.COPPER.getInfoName());

		return String.join(", ", roles);
	}

	private static String traits(OreSetDefinition set)
	{
		List<String> traits = new ArrayList<>();
		ItemTraits item = set.item();
		StorageBlockTraits block = set.block();

		addIf(traits, item.shiny(), "shiny");
		addIf(traits, item.firestarter(), "burns");
		item.food().ifPresent(food -> traits.add("food{amount=" + food.nutrition() + ", saturation=" + food.saturation() + (food.wolfFood() ? ", wolf food" : "") + (food.alwaysEdible() ? ", always edible" : "") + "}"));
		addIf(traits, block.falls(), "falls");
		addIf(traits, block.lightLevel() != 0, "glow{" + block.lightLevel() + "}");
		addIf(traits, block.lightOpacity() != StorageBlockTraits.OPAQUE, "opacity{" + Math.round((block.lightOpacity() / 255.0) * 100.0) + "%}");
		addIf(traits, block.absorbsWater(), "absorbs");
		addIf(traits, block.slipperiness() != StorageBlockTraits.DEFAULT_SLIPPERINESS, "slipperiness{" + (block.slipperiness() - StorageBlockTraits.DEFAULT_SLIPPERINESS) + "}");
		addIf(traits, block.acceleration() != 1, "acceleration{x" + block.acceleration() + "}");
		addIf(traits, block.walkDamage() != 0, "walk damage{" + block.walkDamage() + "}");
		// The misspelling is the 1.12 text, kept word for word
		addIf(traits, block.flammability() != FlammabilityOf.STONE, "flammablility of " + block.flammability().name().toLowerCase(Locale.ROOT));

		return String.join(", ", traits);
	}

	private static String harvest(OreSetDefinition set)
	{
		OreGeneration generation = set.generation();
		String harvest = "[level: " + generation.maxHeight() + ", frequency: " + generation.veinAmount() + "-" + generation.veinDensity() + "], needs " + set.block().harvestLevel().name().toLowerCase(Locale.ROOT) + " to harvest";

		return harvest + set.material().map(material -> ", can harvest like " + material.miningLevel().name().toLowerCase(Locale.ROOT)).orElse("");
	}

	private static void addIf(List<String> values, boolean condition, String value)
	{
		if (condition)
		{
			values.add(value);
		}
	}
}
