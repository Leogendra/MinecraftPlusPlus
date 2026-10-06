package fr.minecraftpp;

import java.io.IOException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.minecraftpp.command.MppInfoCommand;
import fr.minecraftpp.config.MppConfigFile;
import fr.minecraftpp.config.WordGenDictionary;
import fr.minecraftpp.content.ContentRegistrar;
import fr.minecraftpp.content.CreativeTabEntries;
import fr.minecraftpp.content.FlammabilityRegistration;
import fr.minecraftpp.content.FuelRegistration;
import fr.minecraftpp.content.RegisteredContent;
import fr.minecraftpp.core.config.MalformedSeedException;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.generator.OreCatalogGenerator;
import fr.minecraftpp.core.text.SetInfoFormatter;
import fr.minecraftpp.core.trait.TraitCatalog;
import fr.minecraftpp.pack.GeneratedPackContents;
import fr.minecraftpp.pack.GeneratedPackWriters;
import fr.minecraftpp.world.OreBiomeModifications;
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
	private static RegisteredContent content;
	private static GeneratedPackContents packContents;

	@Override
	public void onInitialize()
	{
		long seed = readSeed();
		catalog = generateCatalog(seed);
		LOGGER.info("Minecraft++ seed {}, generated ores:\n{}", seed, SetInfoFormatter.format(catalog));

		content = ContentRegistrar.register(catalog);
		CreativeTabEntries.register(catalog, content);
		FuelRegistration.register(catalog, content);
		FlammabilityRegistration.register(catalog, content);
		packContents = GeneratedPackContents.write(catalog, GeneratedPackWriters.all());
		OreBiomeModifications.register(catalog);

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

	/**
	 * The blocks and items registered for the sets of the current game.
	 */
	public static RegisteredContent content()
	{
		if (content == null)
		{
			throw new IllegalStateException("Minecraft++ content is not registered yet");
		}
		else
		{
			return content;
		}
	}

	/**
	 * The files of the generated pack, served to the server data packs and the client resource packs.
	 */
	public static GeneratedPackContents packContents()
	{
		if (packContents == null)
		{
			throw new IllegalStateException("The Minecraft++ generated pack is not written yet");
		}
		else
		{
			return packContents;
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
