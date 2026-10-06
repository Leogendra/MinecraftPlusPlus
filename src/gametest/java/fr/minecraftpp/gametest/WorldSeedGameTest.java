package fr.minecraftpp.gametest;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.core.world.WorldSeedStatus;
import fr.minecraftpp.world.MppSeedFile;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.storage.LevelResource;

/**
 * The game test server creates a new world at each run: it gets the seed file of the configured seed.
 */
public class WorldSeedGameTest
{
	@GameTest
	public void aNewWorldStoresTheConfiguredSeed(GameTestHelper helper)
	{
		MppSeedFile seedFile = new MppSeedFile(helper.getLevel().getServer().getWorldPath(LevelResource.ROOT));

		helper.assertTrue(seedFile.status(MinecraftPlusPlus.catalog().seed()) == WorldSeedStatus.VALID, "the new world has no valid seed file at " + seedFile.getPath());
		helper.succeed();
	}
}
