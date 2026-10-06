package fr.minecraftpp.client.world;

import java.io.IOException;
import java.util.Optional;

import fr.minecraftpp.core.config.MalformedSeedException;
import fr.minecraftpp.core.world.WorldSeedStatus;
import fr.minecraftpp.world.MppSeedFile;
import net.minecraft.network.chat.Component;

/**
 * The texts that tell the player why a world cannot be opened with the current Minecraft++ seed, and what to do, as 1.12 did.
 */
public final class WorldSeedTexts
{
	private WorldSeedTexts()
	{
	}

	public static Component refusalTitle(WorldSeedStatus status)
	{
		return switch (status)
		{
			case VALID -> throw new IllegalArgumentException("A world of the configured seed is never refused");
			case WRONG -> Component.translatable("minecraftpp.world.wrong_seed.title");
			case VANILLA -> Component.translatable("minecraftpp.world.vanilla.title");
		};
	}

	/**
	 * The world needs another seed, named when its seed file can be read, or it was created without the mod.
	 */
	public static Component refusalMessage(WorldSeedStatus status, MppSeedFile seedFile)
	{
		return switch (status)
		{
			case VALID -> throw new IllegalArgumentException("A world of the configured seed is never refused");
			case WRONG -> readableSeed(seedFile).map(seed -> Component.translatable("minecraftpp.world.wrong_seed.message", String.valueOf(seed))).orElse(Component.translatable("minecraftpp.world.unreadable_seed.message"));
			case VANILLA -> Component.translatable("minecraftpp.world.vanilla.message");
		};
	}

	private static Optional<Long> readableSeed(MppSeedFile seedFile)
	{
		try
		{
			return seedFile.read();
		}
		catch (IOException | MalformedSeedException exception)
		{
			return Optional.empty();
		}
	}
}
