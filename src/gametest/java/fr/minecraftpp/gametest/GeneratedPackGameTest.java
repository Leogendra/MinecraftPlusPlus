package fr.minecraftpp.gametest;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.content.material.MaterialFactory;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.material.ToolType;
import fr.minecraftpp.pack.GeneratedPackSource;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;

/**
 * ing (metal) and brumed (material) are the equipped sets of the game test seed; imer has no tools.
 */
public class GeneratedPackGameTest
{
	@GameTest
	public void generatedPackIsActiveOnTheServer(GameTestHelper helper)
	{
		helper.assertTrue(helper.getLevel().getServer().getPackRepository().getSelectedIds().contains(GeneratedPackSource.PACK_ID), "the generated pack is not selected");
		helper.succeed();
	}

	@GameTest
	public void generatedRepairTagsRepairTheTools(GameTestHelper helper)
	{
		for (String setName : new String[] { "ing", "brumed" })
		{
			OreSetDefinition set = GameTestSets.set(setName);
			ItemStack mainItem = new ItemStack(GameTestSets.item(setName));
			ItemStack pickaxe = new ItemStack(MinecraftPlusPlus.content().item(ContentIds.tool(set, ToolType.PICKAXE)));

			helper.assertTrue(mainItem.is(MaterialFactory.repairMaterials(set)), setName + " is missing from its generated repair tag");
			helper.assertTrue(pickaxe.isValidRepairItem(mainItem), setName + " does not repair its pickaxe");
			helper.assertFalse(pickaxe.isValidRepairItem(new ItemStack(GameTestSets.item("imer"))), "imer repairs the " + setName + " pickaxe");
		}

		helper.succeed();
	}
}
