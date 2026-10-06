package fr.minecraftpp.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FuelValues;

/**
 * coms is a fuel and voging the coal of the game test seed.
 */
public class FuelGameTest
{
	@GameTest
	public void furnacesBurnTheGeneratedFuels(GameTestHelper helper)
	{
		FuelValues fuelValues = helper.getLevel().fuelValues();

		for (String setName : new String[] { "coms", "voging" })
		{
			int expected = GameTestSets.set(setName).item().fuelTicks();
			int burnDuration = fuelValues.burnDuration(new ItemStack(GameTestSets.item(setName)));

			helper.assertTrue(expected > 0 && burnDuration == expected, setName + " burns " + burnDuration + " ticks instead of " + expected);
		}

		helper.assertTrue(fuelValues.burnDuration(new ItemStack(GameTestSets.item("imer"))) == 0, "imer is not a fuel");
		helper.succeed();
	}
}
