package fr.minecraftpp.pack.vanilla;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.IoSupplier;

/**
 * Reads the vanilla data from the Minecraft jar, through the same vanilla pack the server loads.
 */
public final class VanillaPackData implements VanillaData
{
	@Override
	public Map<Identifier, JsonElement> files(String directory)
	{
		Map<Identifier, JsonElement> files = new TreeMap<>();

		try (VanillaPackResources vanillaPack = ServerPacksSource.createVanillaPackSource())
		{
			vanillaPack.listResources(PackType.SERVER_DATA, Identifier.DEFAULT_NAMESPACE, directory, (location, opener) -> files.put(location, parse(location, opener)));
		}

		return files;
	}

	private static JsonElement parse(Identifier location, IoSupplier<InputStream> opener)
	{
		try (InputStream stream = opener.get())
		{
			return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		}
		catch (IOException exception)
		{
			throw new UncheckedIOException("Cannot read the vanilla file " + location, exception);
		}
	}
}
