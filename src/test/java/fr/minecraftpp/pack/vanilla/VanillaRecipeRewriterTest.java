package fr.minecraftpp.pack.vanilla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.SampleVanillaData;
import net.minecraft.resources.Identifier;

/**
 * Seed 42: kal is the iron set (a material, so the iron block has a variant), cychotinte the gold set and derepess the coal set. The vanilla files are the hand-written samples.
 */
class VanillaRecipeRewriterTest
{
	private static final Map<Identifier, JsonElement> FILES = new VanillaRecipeRewriter(new SampleVanillaData()).write(TestCatalogs.generate(42)).stream().collect(Collectors.toMap(GeneratedFile::location, file -> JsonParser.parseString(file.content())));

	@Test
	void anItemWithVariantsBecomesItsVariantTag()
	{
		assertEquals(JsonParser.parseString("{\"type\":\"minecraft:crafting_shaped\",\"key\":{\"#\":\"#minecraftpp:variants/iron_ingot\"},\"pattern\":[\"# #\",\" # \"],\"result\":{\"id\":\"minecraft:bucket\"}}"), FILES.get(Identifier.parse("minecraft:recipe/bucket.json")));
	}

	@Test
	void aListOfItemsReceivesTheVariants()
	{
		JsonElement torch = FILES.get(Identifier.parse("minecraft:recipe/torch.json"));

		assertEquals(JsonParser.parseString("[\"minecraft:coal\",\"minecraftpp:derepess\",\"minecraft:charcoal\"]"), torch.getAsJsonObject().getAsJsonObject("key").get("X"));
	}

	/**
	 * Regression: the elements of a shapeless list are each an ingredient, and once received the variants as extra ingredients, which the game refused (12 ingredients for 9 slots).
	 */
	@Test
	void eachElementOfAShapelessListIsOneIngredient()
	{
		JsonElement netheriteIngot = FILES.get(Identifier.parse("minecraft:recipe/netherite_ingot.json"));

		assertEquals(JsonParser.parseString("[\"minecraft:netherite_scrap\",\"minecraft:netherite_scrap\",\"#minecraftpp:variants/gold_ingot\",\"#minecraftpp:variants/gold_ingot\"]"), netheriteIngot.getAsJsonObject().get("ingredients"));
	}

	@Test
	void aVanillaTagWithVariantsBecomesATagOfTheTagAndItsVariants()
	{
		JsonElement campfire = FILES.get(Identifier.parse("minecraft:recipe/campfire.json"));

		assertEquals("#minecraftpp:variants/tag/coals", campfire.getAsJsonObject().getAsJsonObject("key").get("C").getAsString());
		assertEquals("#minecraft:logs", campfire.getAsJsonObject().getAsJsonObject("key").get("L").getAsString());
		assertEquals(JsonParser.parseString("{\"values\":[\"#minecraft:coals\",\"minecraftpp:derepess\"]}"), FILES.get(Identifier.parse("minecraftpp:tags/item/variants/tag/coals.json")));
	}

	/**
	 * Nine generated ingots would otherwise make both the generated block and the vanilla one.
	 */
	@Test
	void aRecipeWhoseResultHasVariantsStaysVanilla()
	{
		assertFalse(FILES.containsKey(Identifier.parse("minecraft:recipe/iron_block.json")));
	}

	@Test
	void aRecipeWithoutVariantsStaysVanilla()
	{
		assertFalse(FILES.containsKey(Identifier.parse("minecraft:recipe/stick.json")));
	}

	@Test
	void eachVanillaItemWithVariantsHasAVariantTag()
	{
		Map<Identifier, JsonElement> tags = new VariantTagWriter().write(TestCatalogs.generate(42)).stream().collect(Collectors.toMap(GeneratedFile::location, file -> JsonParser.parseString(file.content())));

		assertEquals(JsonParser.parseString("{\"values\":[\"minecraft:iron_ingot\",\"minecraftpp:kal\"]}"), tags.get(Identifier.parse("minecraftpp:tags/item/variants/iron_ingot.json")));
	}
}
