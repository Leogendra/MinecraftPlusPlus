package fr.minecraftpp.gametest;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.core.set.ContentIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.trading.VillagerTrade;

/**
 * In the game test seed, formutlest is the currency and ing the iron set.
 */
public class VillagerTradeGameTest
{
	@GameTest
	public void theSmithBuysGeneratedIronForTheCurrency(GameTestHelper helper)
	{
		Registry<VillagerTrade> trades = helper.getLevel().registryAccess().lookupOrThrow(Registries.VILLAGER_TRADE);
		VillagerTrade trade = trades.getValueOrThrow(ResourceKey.create(Registries.VILLAGER_TRADE, Identifier.withDefaultNamespace("smith/2/iron_ingot_emerald")));
		JsonElement json = VillagerTrade.CODEC.encodeStart(helper.getLevel().registryAccess().createSerializationContext(JsonOps.INSTANCE), trade).getOrThrow();

		helper.assertTrue(json.getAsJsonObject().getAsJsonObject("gives").get("id").getAsString().equals(ContentIds.full(ContentIds.item(GameTestSets.set("formutlest")))), "the smith pays with " + json);
		helper.assertTrue(json.getAsJsonObject().getAsJsonObject("wants").get("id").getAsString().equals(ContentIds.full(ContentIds.item(GameTestSets.set("ing")))), "the smith wants " + json);
		helper.succeed();
	}

	/**
	 * A rewritten trade the game cannot read is only logged, and the trade disappears.
	 */
	@GameTest
	public void theGameReadsEveryRewrittenTrade(GameTestHelper helper)
	{
		Registry<VillagerTrade> trades = helper.getLevel().registryAccess().lookupOrThrow(Registries.VILLAGER_TRADE);

		for (Identifier location : MinecraftPlusPlus.packContents().resources(PackType.SERVER_DATA, Identifier.DEFAULT_NAMESPACE, "villager_trade").keySet())
		{
			Identifier tradeId = location.withPath(path -> path.substring("villager_trade/".length(), path.length() - ".json".length()));

			helper.assertTrue(trades.containsKey(tradeId), "the game did not load the rewritten trade " + tradeId);
		}

		helper.succeed();
	}
}
