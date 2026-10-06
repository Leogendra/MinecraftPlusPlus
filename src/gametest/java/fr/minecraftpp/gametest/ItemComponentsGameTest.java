package fr.minecraftpp.gametest;

import fr.minecraftpp.content.item.ItemPropertiesFactory;
import fr.minecraftpp.core.set.FoodDefinition;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public class ItemComponentsGameTest
{
	@GameTest
	public void edibleItemHasTheGeneratedFoodValues(GameTestHelper helper)
	{
		FoodDefinition food = GameTestSets.set("voging").item().food().orElseThrow();
		FoodProperties properties = new ItemStack(GameTestSets.item("voging")).get(DataComponents.FOOD);

		helper.assertTrue(properties != null, "voging should be edible");
		helper.assertTrue(properties.nutrition() == food.nutrition(), "nutrition " + properties.nutrition() + " instead of " + food.nutrition());
		helper.assertTrue(properties.saturation() == ItemPropertiesFactory.foodProperties(food).saturation(), "unexpected saturation " + properties.saturation());
		helper.succeed();
	}

	@GameTest
	public void shinyItemHasTheGlint(GameTestHelper helper)
	{
		helper.assertTrue(new ItemStack(GameTestSets.item("ing")).hasFoil(), "the ing ingot should be shiny");
		helper.assertTrue(!new ItemStack(GameTestSets.item("imer")).hasFoil(), "imer should not be shiny");
		helper.succeed();
	}

	@GameTest
	public void nameHasTheColorOfTheRarity(GameTestHelper helper)
	{
		ItemStack familiar = new ItemStack(GameTestSets.item("formutlest"));
		TextColor color = familiar.getHoverName().getStyle().getColor();

		helper.assertTrue(color != null && color.getValue() == 0x55FF55, "a familiar item should be named in green, got " + color);
		helper.succeed();
	}
}
