package fr.minecraftpp.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;

/**
 * In the game test seed, imer is the enchanting currency.
 */
public class EnchantingCurrencyGameTest
{
	private static final int CURRENCY_SLOT = 1;
	private static final int FIRST_INVENTORY_SLOT = 2;

	@GameTest
	public void theEnchantingTableTakesTheCurrencyAndRefusesLapis(GameTestHelper helper)
	{
		Player player = helper.makeMockPlayer(GameType.SURVIVAL);
		Slot currencySlot = new EnchantmentMenu(0, player.getInventory()).getSlot(CURRENCY_SLOT);

		helper.assertTrue(currencySlot.mayPlace(new ItemStack(GameTestSets.item("imer"))), "the enchanting table refuses the generated currency");
		helper.assertFalse(currencySlot.mayPlace(new ItemStack(Items.LAPIS_LAZULI)), "the enchanting table still takes lapis lazuli");
		helper.assertTrue(currencySlot.getNoItemIcon() == null, "the empty currency slot still shows lapis lazuli");
		helper.succeed();
	}

	@GameTest
	public void shiftClickMovesTheCurrencyIntoItsSlot(GameTestHelper helper)
	{
		Player player = helper.makeMockPlayer(GameType.SURVIVAL);
		EnchantmentMenu menu = new EnchantmentMenu(0, player.getInventory());
		menu.getSlot(FIRST_INVENTORY_SLOT).set(new ItemStack(GameTestSets.item("imer"), 5));

		menu.quickMoveStack(player, FIRST_INVENTORY_SLOT);

		helper.assertTrue(menu.getSlot(CURRENCY_SLOT).getItem().is(GameTestSets.item("imer")), "the currency was not moved into its slot");
		helper.succeed();
	}
}
