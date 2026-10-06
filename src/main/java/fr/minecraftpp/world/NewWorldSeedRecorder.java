package fr.minecraftpp.world;

import java.io.IOException;
import java.io.UncheckedIOException;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

/**
 * Stores the configured seed in every world created with the mod, as 1.12 did when the world was created. A world is new while its data is not initialized, until its levels are created, right after the server starts.
 *
 * The recording happens on the server, so the worlds of a dedicated server get their seed file too.
 */
public final class NewWorldSeedRecorder
{
	private NewWorldSeedRecorder()
	{
	}

	public static void register(long seed)
	{
		ServerLifecycleEvents.SERVER_STARTING.register(server -> recordIfNew(server, seed));
	}

	private static void recordIfNew(MinecraftServer server, long seed)
	{
		if (!server.getWorldData().overworldData().isInitialized())
		{
			MppSeedFile seedFile = new MppSeedFile(server.getWorldPath(LevelResource.ROOT));

			try
			{
				seedFile.write(seed);
			}
			catch (IOException exception)
			{
				throw new UncheckedIOException("Cannot store the Minecraft++ seed of the new world in " + seedFile.getPath(), exception);
			}
		}
	}
}
