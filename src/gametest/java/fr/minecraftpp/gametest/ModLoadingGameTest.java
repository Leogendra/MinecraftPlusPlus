package fr.minecraftpp.gametest;

import fr.minecraftpp.MinecraftPlusPlus;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.gametest.framework.GameTestHelper;

public class ModLoadingGameTest
{
	@GameTest
	public void serverStartsWithTheModLoaded(GameTestHelper helper)
	{
		helper.assertTrue(FabricLoader.getInstance().isModLoaded(MinecraftPlusPlus.MOD_ID), "minecraftpp is not loaded");
		helper.succeed();
	}
}
