package fr.minecraftpp.core.set.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.GoldenFixture;
import fr.minecraftpp.core.GoldenFixture.GoldenSet;
import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreDrop;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.MaterialDefinition;
import fr.minecraftpp.core.set.material.ToolType;
import fr.minecraftpp.core.text.DisplayNameFormatter;
import fr.minecraftpp.core.text.SetInfoFormatter;

/**
 * Main regression test of the port: for each golden seed, the generated catalog must match the 1.12 capture value for value.
 */
class OreCatalogGoldenTest
{
	private static final Map<SetType, String> LEGACY_CLASS_NAMES = Map.of(SetType.SIMPLE, "SimpleSet", SetType.MATERIAL, "MaterialSet", SetType.METAL, "MetalSet");
	private static final Map<VanillaRole, String> LEGACY_ROLE_FLAGS = Map.of(VanillaRole.BLUE_DYE, "BlueDye", VanillaRole.REDSTONE, "Redstone", VanillaRole.CURRENCY, "Currency", VanillaRole.FUEL, "Fuel", VanillaRole.BEACON, "Beacon", VanillaRole.ENCHANTING_CURRENCY, "EnchantCurrency", VanillaRole.COAL, "Coal", VanillaRole.IRON, "Iron", VanillaRole.GOLD, "Gold", VanillaRole.DIAMOND, "Diamond");

	@Test
	void mppInfoTextMatchesThe112Text()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			OreCatalog catalog = TestCatalogs.generate(seed);

			assertEquals(String.join("\n", GoldenFixture.load(seed).infoLines()), SetInfoFormatter.format(catalog), "seed " + seed);
		}
	}

	@Test
	void everyGeneratedValueMatchesThe112Value()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			List<OreSetDefinition> sets = TestCatalogs.generate(seed).sets();
			List<GoldenSet> goldenSets = GoldenFixture.load(seed).sets();

			assertEquals(goldenSets.size(), sets.size(), "seed " + seed);
			for (int index = 0; index < sets.size(); index++)
			{
				assertEquals(goldenSets.get(index).type(), LEGACY_CLASS_NAMES.get(sets.get(index).type()), "type, seed " + seed + ", set " + index);
				assertEquals(goldenSets.get(index).values(), legacyDetails(sets.get(index)), "seed " + seed + ", set " + index);
			}
		}
	}

	@Test
	void displayNamesMatchThe112Translations()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			OreCatalog catalog = TestCatalogs.generate(seed);
			Map<String, String> translations = GoldenFixture.load(seed).translations();

			List<String> expectedNames = translations.entrySet().stream().filter(entry -> !entry.getKey().startsWith("death.")).map(Map.Entry::getValue).sorted().toList();
			List<String> expectedDeathMessages = translations.entrySet().stream().filter(entry -> entry.getKey().startsWith("death.")).map(Map.Entry::getValue).sorted().toList();

			assertEquals(expectedNames, displayNames(catalog), "seed " + seed);
			assertEquals(expectedDeathMessages, catalog.sets().stream().filter(set -> set.block().walkDamage() > 0).map(DisplayNameFormatter::walkDamageDeathMessage).sorted().toList(), "seed " + seed);
		}
	}

	private static List<String> displayNames(OreCatalog catalog)
	{
		List<String> names = new ArrayList<>();

		for (OreSetDefinition set : catalog.sets())
		{
			names.add(DisplayNameFormatter.itemName(set));
			names.add(DisplayNameFormatter.storageBlockName(set));
			names.add(DisplayNameFormatter.oreName(set));

			if (set.type() == SetType.METAL)
			{
				names.add(DisplayNameFormatter.nuggetName(set));
			}

			if (set.type().hasMaterial())
			{
				Arrays.stream(ToolType.values()).map(toolType -> DisplayNameFormatter.toolName(set, toolType)).forEach(names::add);
				Arrays.stream(ArmorPiece.values()).map(piece -> DisplayNameFormatter.armorName(set, piece)).forEach(names::add);
			}
		}

		return names.stream().sorted().toList();
	}

	/**
	 * Renders a definition in the format of the capture program described in src/test/resources/golden/README.md.
	 */
	private static Map<String, String> legacyDetails(OreSetDefinition set)
	{
		Map<String, String> details = new LinkedHashMap<>();

		details.put("name", set.name());
		details.put("rarity", set.rarity().name());
		details.put("roles", set.roles().stream().map(LEGACY_ROLE_FLAGS::get).collect(Collectors.joining(",")));
		details.put("ore_rarity", set.generation().veinAmount() + "," + set.generation().veinDensity() + "," + set.generation().maxHeight());
		putItemDetails(details, set);
		putBlockDetails(details, set);
		putOreDetails(details, set);
		set.material().ifPresent(material -> putMaterialDetails(details, material));

		return details;
	}

	private static void putItemDetails(Map<String, String> details, OreSetDefinition set)
	{
		details.put("item.texture", String.valueOf(set.item().textureId()));
		details.put("item.color", set.item().color().toString());
		details.put("item.shiny", String.valueOf(set.item().shiny()));
		details.put("item.fuel", String.valueOf(set.item().fuelTicks()));
		details.put("item.enchant_currency", String.valueOf(set.item().enchantingCurrency()));
		details.put("item.puts_fire", String.valueOf(set.item().firestarter()));
		details.put("item.beacon_currency", String.valueOf(set.item().beaconPayment()));
		details.put("item.food", set.item().food().map(food -> food.nutrition() + "," + food.saturation() + "," + food.wolfFood() + "," + food.alwaysEdible()).orElse("none"));
	}

	private static void putBlockDetails(Map<String, String> details, OreSetDefinition set)
	{
		details.put("block.texture", String.valueOf(set.block().textureId()));
		details.put("block.harvest_level", String.valueOf(set.block().harvestLevel().ordinal()));
		details.put("block.gravity", String.valueOf(set.block().falls()));
		details.put("block.absorbing", String.valueOf(set.block().absorbsWater()));
		details.put("block.walk_damage", String.valueOf(set.block().walkDamage()));
		details.put("block.flammability", set.block().flammability().name());
		details.put("block.acceleration", String.valueOf(set.block().acceleration()));
		details.put("block.beacon_base", String.valueOf(set.block().beaconBase()));
		details.put("block.redstone_power", String.valueOf(set.block().redstonePower()));
		details.put("block.fuel", String.valueOf(set.block().fuelTicks()));
		details.put("block.light", String.valueOf(set.block().lightLevel()));
		details.put("block.opacity", String.valueOf(set.block().lightOpacity()));
		details.put("block.slipperiness", String.valueOf(set.block().slipperiness()));
	}

	private static void putOreDetails(Map<String, String> details, OreSetDefinition set)
	{
		details.put("ore.texture", String.valueOf(set.ore().textureId()));
		details.put("ore.harvest_level", String.valueOf(set.ore().harvestLevel().ordinal()));

		if (set.ore().drop() instanceof OreDrop.Items items)
		{
			details.put("ore.drops", items.minimum() + "-" + items.maximum());
			details.put("ore.xp", items.minimumExperience() + "-" + items.maximumExperience());
			details.put("ore.powered", String.valueOf(set.ore().powered()));
		}
		else
		{
			details.put("ore.drops", "self");
		}
	}

	private static void putMaterialDetails(Map<String, String> details, MaterialDefinition material)
	{
		details.put("material.tier", String.valueOf(material.tier()));
		details.put("material.texture", String.valueOf(material.textureId()));
		details.put("material.harvest_level", String.valueOf(material.miningLevel().ordinal()));
		details.put("material.durability", String.valueOf(material.tools().durability()));
		details.put("material.efficiency", String.valueOf(material.tools().efficiency()));
		details.put("material.attack_damage", floatArray(material.tools().damageBonus()));
		details.put("material.attack_speed", floatArray(material.tools().speedBonus()));
		details.put("material.armor_durability_factor", String.valueOf(material.armor().durabilityFactor()));
		details.put("material.armor_reduction", Arrays.stream(ArmorPiece.values()).map(piece -> String.valueOf(material.armor().defense(piece))).collect(Collectors.joining(", ", "[", "]")));
		details.put("material.toughness", String.valueOf(material.armor().toughness()));
		details.put("material.enchantability", String.valueOf(material.enchantability()));
	}

	private static String floatArray(Map<ToolType, Float> values)
	{
		return Arrays.stream(ToolType.values()).map(toolType -> String.valueOf(values.get(toolType))).collect(Collectors.joining(", ", "[", "]"));
	}
}
