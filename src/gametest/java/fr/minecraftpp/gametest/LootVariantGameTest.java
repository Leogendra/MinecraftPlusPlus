package fr.minecraftpp.gametest;

import java.util.ArrayList;
import java.util.List;

import fr.minecraftpp.MinecraftPlusPlus;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

/**
 * In the game test seed, ing is the iron set: the vanilla chests give its ingot instead of the vanilla one.
 */
public class LootVariantGameTest
{
	private static final ResourceKey<LootTable> WEAPONSMITH_CHEST = ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("chests/village/village_weaponsmith"));

	@GameTest
	public void chestsGiveTheGeneratedIronInsteadOfTheVanillaOne(GameTestHelper helper)
	{
		LootTable table = helper.getLevel().getServer().reloadableRegistries().getLootTable(WEAPONSMITH_CHEST);
		LootParams params = new LootParams.Builder(helper.getLevel()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(helper.absolutePos(BlockPos.ZERO))).create(LootContextParamSets.CHEST);
		List<ItemStack> loot = new ArrayList<>();

		for (long seed = 0; seed < 50; seed++)
		{
			loot.addAll(table.getRandomItems(params, seed));
		}

		helper.assertFalse(loot.stream().anyMatch(stack -> stack.is(Items.IRON_INGOT)), "a vanilla iron ingot is still in the chest loot");
		helper.assertTrue(loot.stream().anyMatch(stack -> stack.is(GameTestSets.item("ing"))), "the generated iron is not in the chest loot: " + loot);
		helper.succeed();
	}

	/**
	 * A rewritten table the game cannot read is only logged, and the vanilla table is replaced by an empty one.
	 */
	@GameTest
	public void theGameReadsEveryRewrittenLootTable(GameTestHelper helper)
	{
		for (Identifier location : MinecraftPlusPlus.packContents().resources(PackType.SERVER_DATA, Identifier.DEFAULT_NAMESPACE, "loot_table").keySet())
		{
			ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, location.withPath(path -> path.substring("loot_table/".length(), path.length() - ".json".length())));

			helper.assertTrue(helper.getLevel().getServer().reloadableRegistries().getLootTable(key) != LootTable.EMPTY, "the game did not load the rewritten loot table " + key.identifier());
		}

		helper.succeed();
	}
}
