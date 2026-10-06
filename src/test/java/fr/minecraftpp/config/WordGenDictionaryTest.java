package fr.minecraftpp.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class WordGenDictionaryTest
{
	@Test
	void loadsTheEmbeddedDictionary() throws IOException
	{
		// The first name of seed 42 in the 1.12 capture
		assertEquals("cychotinte", WordGenDictionary.nameGenerator(42L).nextName());
	}
}
