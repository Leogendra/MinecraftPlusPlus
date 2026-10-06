package fr.minecraftpp.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads a golden fixture captured from the 1.12 generator (see src/test/resources/golden/README.md).
 */
public class GoldenFixture
{
	public static final List<Long> SEEDS = List.of(26L, 30L, 42L, -7046029254386353131L);

	private final List<String> infoLines;
	private final List<GoldenSet> sets;
	private final Map<String, String> translations;

	private GoldenFixture(List<String> infoLines, List<GoldenSet> sets, Map<String, String> translations)
	{
		this.infoLines = infoLines;
		this.sets = sets;
		this.translations = translations;
	}

	public static GoldenFixture load(long seed)
	{
		String resource = "/golden/seed-" + seed + ".txt";

		try (InputStream stream = GoldenFixture.class.getResourceAsStream(resource))
		{
			if (stream == null)
			{
				throw new IllegalArgumentException("Missing golden fixture " + resource);
			}

			return parse(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
		}
		catch (IOException exception)
		{
			throw new UncheckedIOException(exception);
		}
	}

	private static GoldenFixture parse(String content)
	{
		List<String> infoLines = new ArrayList<>();
		List<GoldenSet> sets = new ArrayList<>();
		Map<String, String> translations = new LinkedHashMap<>();
		String section = "";

		for (String line : content.split("\r?\n"))
		{
			if (line.startsWith("# "))
			{
				section = line.substring(2);
			}
			else if (section.equals("info"))
			{
				infoLines.add(line);
			}
			else if (section.equals("details") && line.startsWith("set "))
			{
				sets.add(new GoldenSet(line.split(" ")[2], new LinkedHashMap<>()));
			}
			else if (section.equals("details"))
			{
				String entry = line.trim();
				int separator = entry.indexOf('=');
				sets.getLast().values().put(entry.substring(0, separator), entry.substring(separator + 1));
			}
			else if (section.equals("translations"))
			{
				int separator = line.indexOf('=');
				translations.put(line.substring(0, separator), line.substring(separator + 1));
			}
		}

		return new GoldenFixture(infoLines, sets, translations);
	}

	public List<String> infoLines()
	{
		return this.infoLines;
	}

	public List<GoldenSet> sets()
	{
		return this.sets;
	}

	public Map<String, String> translations()
	{
		return this.translations;
	}

	/**
	 * One generated set: its 1.12 class name (SimpleSet, MaterialSet or MetalSet) and its captured values.
	 */
	public record GoldenSet(String type, Map<String, String> values)
	{
		public String value(String key)
		{
			String value = this.values.get(key);

			if (value == null)
			{
				throw new IllegalArgumentException("No golden value " + key);
			}
			else
			{
				return value;
			}
		}
	}
}
