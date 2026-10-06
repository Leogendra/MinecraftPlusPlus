package fr.minecraftpp.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.GoldenFixture;

class NameGeneratorTest
{
	@Test
	void namesMatchThe112Generator() throws IOException
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			List<String> expectedNames = GoldenFixture.load(seed).sets().stream().map(set -> set.value("name")).toList();

			assertEquals(expectedNames, generateNames(seed, expectedNames.size()), "seed " + seed);
		}
	}

	@Test
	void sameSeedGivesTheSameNames() throws IOException
	{
		assertEquals(generateNames(123L, 10), generateNames(123L, 10));
	}

	private static List<String> generateNames(long seed, int count) throws IOException
	{
		try (InputStream stream = NameGeneratorTest.class.getResourceAsStream("/minecraftpp/wordGen.mpp"))
		{
			assertNotNull(stream, "the dictionary is embedded in the mod resources");
			NameGenerator generator = new NameGenerator(seed, new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
			List<String> names = new ArrayList<>();

			for (int i = 0; i < count; i++)
			{
				names.add(generator.nextName());
			}

			return names;
		}
	}
}
