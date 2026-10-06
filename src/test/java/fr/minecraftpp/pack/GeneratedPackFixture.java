package fr.minecraftpp.pack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.core.set.ContentIds;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;

/**
 * Writes the complete generated pack of a seed, over the sample vanilla files, and reads its files as JSON, next to the reference files of src/test/resources/pack.
 */
public final class GeneratedPackFixture
{
	private GeneratedPackFixture()
	{
	}

	public static GeneratedPackContents write(long seed)
	{
		return GeneratedPackContents.write(TestCatalogs.generate(seed), GeneratedPackWriters.all(new SampleVanillaData()));
	}

	/**
	 * Every file of the mod namespace, by location.
	 */
	public static Map<Identifier, JsonElement> files(GeneratedPackContents contents, PackType type)
	{
		Map<Identifier, JsonElement> files = new LinkedHashMap<>();

		contents.resources(type, ContentIds.NAMESPACE, "").forEach((location, opener) -> files.put(location, parse(opener)));

		return files;
	}

	/**
	 * A generated file, or a failure when it is missing.
	 *
	 * @param location the namespace and the path below it, such as {@code minecraftpp:items/xyzium.json}
	 */
	public static JsonElement file(GeneratedPackContents contents, PackType type, Identifier location)
	{
		return parse(contents.resource(type, location).orElseThrow(() -> new AssertionError("Missing generated file " + type.getDirectory() + "/" + location.getNamespace() + "/" + location.getPath())));
	}

	/**
	 * The reference file stored at the same place as the generated one, below src/test/resources/pack.
	 */
	public static JsonElement reference(PackType type, Identifier location)
	{
		return parse(() -> GeneratedPackFixture.class.getResourceAsStream("/pack/" + type.getDirectory() + "/" + location.getNamespace() + "/" + location.getPath()));
	}

	private static JsonElement parse(IoSupplier<InputStream> opener)
	{
		try (InputStream stream = opener.get())
		{
			if (stream == null)
			{
				throw new IllegalArgumentException("Missing JSON file");
			}

			return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		}
		catch (IOException exception)
		{
			throw new UncheckedIOException(exception);
		}
	}
}
