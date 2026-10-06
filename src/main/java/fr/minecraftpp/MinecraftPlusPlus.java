package fr.minecraftpp;

import java.io.IOException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.minecraftpp.command.MppInfoCommand;
import fr.minecraftpp.config.MppConfigFile;
import fr.minecraftpp.config.WordGenDictionary;
import fr.minecraftpp.core.config.MalformedSeedException;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.generator.OreCatalogGenerator;
import fr.minecraftpp.core.text.SetInfoFormatter;
import fr.minecraftpp.core.trait.TraitCatalog;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Common entry point: reads the Minecraft++ seed, generates the seven sets, then plugs each module into the game.
 */
public class MinecraftPlusPlus implements ModInitializer
{
	public static final String MOD_ID = ContentIds.NAMESPACE;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static OreCatalog catalog;

	@Override
	public void onInitialize()
	{
		long seed = readSeed();
		catalog = generateCatalog(seed);

		LOGGER.info("Minecraft++ seed {}, generated ores:\n{}", seed, SetInfoFormatter.format(catalog));

		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> MppInfoCommand.register(dispatcher, catalog));
	}

	/**
	 * The sets of the current game. The content, the generated pack and the mixins read them once the mod is initialized.
	 */
	public static OreCatalog catalog()
	{
		if (catalog == null)
		{
			throw new IllegalStateException("Minecraft++ is not initialized yet");
		}
		else
		{
			return catalog;
		}
	}

	private static long readSeed()
	{
		MppConfigFile configFile = new MppConfigFile(FabricLoader.getInstance().getConfigDir());

		try
		{
			return configFile.readOrCreate(new Random());
		}
		catch (IOException exception)
		{
			throw new IllegalStateException("Cannot read or create the Minecraft++ seed file " + configFile.getPath(), exception);
		}
		catch (MalformedSeedException exception)
		{
			throw new IllegalStateException("The Minecraft++ seed file " + configFile.getPath() + " is malformed: " + exception.getMessage() + ". Fix it or delete it to get a new random seed.", exception);
		}
	}

	private static OreCatalog generateCatalog(long seed)
	{
		try
		{
			return OreCatalogGenerator.generate(seed, WordGenDictionary.nameGenerator(seed), TraitCatalog.defaults());
		}
		catch (IOException exception)
		{
			throw new IllegalStateException("Cannot read the Minecraft++ name dictionary", exception);
		}
	}
}
