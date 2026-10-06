package fr.minecraftpp.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import fr.minecraftpp.core.naming.NameGenerator;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.generator.OreCatalogGenerator;
import fr.minecraftpp.core.set.generator.RoleScope;
import fr.minecraftpp.core.trait.TraitCatalog;

/**
 * Generates catalogs for the tests, with the dictionary embedded in the mod resources and the 1.12 trait chances.
 */
public final class TestCatalogs
{
	private TestCatalogs()
	{
	}

	/**
	 * The sets the game generates, copper included.
	 */
	public static OreCatalog generate(long seed)
	{
		return OreCatalogGenerator.generate(seed, nameGenerator(seed), TraitCatalog.defaults(), RoleScope.WITH_COPPER);
	}

	/**
	 * The sets 1.12 generated, to compare with the golden fixtures.
	 */
	public static OreCatalog generate112(long seed)
	{
		return OreCatalogGenerator.generate(seed, nameGenerator(seed), TraitCatalog.defaults(), RoleScope.MINECRAFT_1_12);
	}

	public static NameGenerator nameGenerator(long seed)
	{
		try (InputStream stream = TestCatalogs.class.getResourceAsStream("/minecraftpp/wordGen.mpp"))
		{
			if (stream == null)
			{
				throw new IllegalStateException("The dictionary is missing from the mod resources");
			}

			return new NameGenerator(seed, new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
		}
		catch (IOException exception)
		{
			throw new UncheckedIOException(exception);
		}
	}
}
