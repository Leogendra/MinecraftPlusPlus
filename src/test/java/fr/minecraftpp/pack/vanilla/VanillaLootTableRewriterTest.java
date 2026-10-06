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
 * Seed 42: kal is the only variant of the iron ingot. The vanilla files are the hand-written samples.
 */
class VanillaLootTableRewriterTest
{
	private static final Map<Identifier, JsonElement> FILES = new VanillaLootTableRewriter(new SampleVanillaData()).write(TestCatalogs.generate(42)).stream().collect(Collectors.toMap(GeneratedFile::location, file -> JsonParser.parseString(file.content())));

	@Test
	void anItemWithVariantsBecomesADrawAmongItsVariantsWithTheSameWeightAndCount()
	{
		JsonElement ironEntry = FILES.get(Identifier.parse("minecraft:loot_table/entities/iron_golem.json")).getAsJsonObject().getAsJsonArray("pools").get(1).getAsJsonObject().getAsJsonArray("entries").get(0);

		assertEquals(JsonParser.parseString("{\"type\":\"minecraft:loot_table\",\"value\":{\"pools\":[{\"rolls\":1.0,\"entries\":[{\"type\":\"minecraft:item\",\"name\":\"minecraftpp:kal\"}]}]},\"weight\":3,\"functions\":[{\"function\":\"minecraft:set_count\",\"count\":{\"type\":\"minecraft:uniform\",\"min\":3.0,\"max\":5.0}}]}"), ironEntry);
	}

	@Test
	void anItemWithoutVariantsIsKept()
	{
		JsonElement poppyEntry = FILES.get(Identifier.parse("minecraft:loot_table/entities/iron_golem.json")).getAsJsonObject().getAsJsonArray("pools").get(0).getAsJsonObject().getAsJsonArray("entries").get(0);

		assertEquals(JsonParser.parseString("{\"type\":\"minecraft:item\",\"name\":\"minecraft:poppy\"}"), poppyEntry);
	}

	@Test
	void blockTablesStayVanilla()
	{
		assertFalse(FILES.containsKey(Identifier.parse("minecraft:loot_table/blocks/iron_block.json")));
	}
}
