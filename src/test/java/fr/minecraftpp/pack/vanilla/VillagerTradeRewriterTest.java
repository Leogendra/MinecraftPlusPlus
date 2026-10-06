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
 * Seed 42: ovic is the currency and kal the iron set, a material with its own boots. The vanilla files are the hand-written samples.
 */
class VillagerTradeRewriterTest
{
	private static final Map<Identifier, JsonElement> FILES = new VillagerTradeRewriter(new SampleVanillaData()).write(TestCatalogs.generate(42)).stream().collect(Collectors.toMap(GeneratedFile::location, file -> JsonParser.parseString(file.content())));

	@Test
	void villagersPayWithTheCurrencyAndWantTheGeneratedIron()
	{
		assertEquals(JsonParser.parseString("{\"gives\":{\"id\":\"minecraftpp:ovic\"},\"wants\":{\"count\":4.0,\"id\":\"minecraftpp:kal\"},\"xp\":10.0}"), FILES.get(Identifier.parse("minecraft:villager_trade/smith/2/iron_ingot_emerald.json")));
	}

	@Test
	void villagersSellTheGeneratedArmorForTheCurrency()
	{
		assertEquals(JsonParser.parseString("{\"gives\":{\"id\":\"minecraftpp:kal_boots\"},\"wants\":{\"count\":4.0,\"id\":\"minecraftpp:ovic\"}}"), FILES.get(Identifier.parse("minecraft:villager_trade/armorer/1/iron_boots.json")));
	}

	@Test
	void aTradeWithoutEmeraldOrVariantStaysVanilla()
	{
		assertFalse(FILES.containsKey(Identifier.parse("minecraft:villager_trade/farmer/1/wheat_bread.json")));
	}
}
