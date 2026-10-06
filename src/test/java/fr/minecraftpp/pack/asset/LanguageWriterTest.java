package fr.minecraftpp.pack.asset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.minecraftpp.core.GoldenFixture;
import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.pack.data.DamageTypeWriter;

class LanguageWriterTest
{
	/**
	 * Every name 1.12 registered is written, with the same text; the deepslate ores are new.
	 */
	@Test
	void namesAndDeathMessagesAreThe112Ones()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			JsonObject language = language(TestCatalogs.generate(seed));
			List<String> written = language.entrySet().stream().filter(entry -> !entry.getKey().startsWith("tag.") && !entry.getKey().endsWith(".player") && !entry.getKey().startsWith("block.minecraftpp.deepslate_")).map(entry -> entry.getValue().getAsString()).sorted().toList();
			List<String> expected = GoldenFixture.load(seed).translations().values().stream().sorted().toList();

			assertEquals(expected, written, "seed " + seed);
		}
	}

	/**
	 * Game test seed: voging hurts the entities walking on its block.
	 */
	@Test
	void theDeathMessagesUseTheMessageOfTheGeneratedDamageType()
	{
		OreCatalog catalog = TestCatalogs.generate(-7046029254386353131L);
		JsonObject language = language(catalog);

		assertEquals("%1$s was killed on Voging Block", language.get("death.attack.minecraftpp.voging_block").getAsString());
		assertEquals("%1$s was killed on Voging Block while fighting %2$s", language.get("death.attack.minecraftpp.voging_block.player").getAsString());
		assertEquals(JsonParser.parseString("{\"message_id\":\"minecraftpp.voging_block\",\"exhaustion\":0.1,\"scaling\":\"when_caused_by_living_non_player\"}"), JsonParser.parseString(new DamageTypeWriter().write(catalog).getFirst().content()));
	}

	@Test
	void theModTagsAreNamed()
	{
		JsonObject language = language(TestCatalogs.generate(42));

		assertEquals("Kal Repair Materials", language.get("tag.item.minecraftpp.kal_repair_materials").getAsString());
		assertEquals("Iron Ingot Variants", language.get("tag.item.minecraftpp.variants.iron_ingot").getAsString());
		assertTrue(language.has("block.minecraftpp.deepslate_kal_ore"));
	}

	private static JsonObject language(OreCatalog catalog)
	{
		JsonElement language = JsonParser.parseString(new LanguageWriter().write(catalog).getFirst().content());

		return language.getAsJsonObject();
	}
}
