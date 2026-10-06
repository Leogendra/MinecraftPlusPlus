package fr.minecraftpp.pack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import fr.minecraftpp.pack.vanilla.VanillaData;
import net.minecraft.resources.Identifier;

/**
 * A few hand-written files in the vanilla format, below src/test/resources/vanilla, standing for the vanilla data in the tests: the real files belong to Mojang and are not copied into the repository.
 */
public final class SampleVanillaData implements VanillaData
{
	private static final List<String> FILES = List.of("recipe/bucket.json", "recipe/torch.json", "recipe/iron_block.json", "recipe/campfire.json", "recipe/stick.json", "recipe/netherite_ingot.json", "tags/item/coals.json", "loot_table/entities/iron_golem.json", "loot_table/blocks/iron_block.json");

	@Override
	public Map<Identifier, JsonElement> files(String directory)
	{
		Map<Identifier, JsonElement> files = new TreeMap<>();

		for (String path : FILES)
		{
			if (path.startsWith(directory + "/"))
			{
				files.put(Identifier.withDefaultNamespace(path), read(path));
			}
		}

		return files;
	}

	private static JsonElement read(String path)
	{
		try (InputStream stream = SampleVanillaData.class.getResourceAsStream("/vanilla/data/minecraft/" + path))
		{
			if (stream == null)
			{
				throw new IllegalStateException("Missing sample vanilla file " + path);
			}

			return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		}
		catch (IOException exception)
		{
			throw new UncheckedIOException(exception);
		}
	}
}
