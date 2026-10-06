package fr.minecraftpp.core.ore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

class ColorTest
{
	@Test
	void packsChannelsAsRgb()
	{
		assertEquals(0x9648BC, new Color(150, 72, 188).asInt());
		assertEquals(0xFFFFFF, Color.WHITE.asInt());
	}

	@Test
	void keepsThe112TextFormat()
	{
		assertEquals("{R: 150, G: 72, B: 188}", new Color(150, 72, 188).toString());
	}

	@Test
	void randomColorsStayInTheRgbRange()
	{
		Random rand = new Random(7);

		for (int i = 0; i < 500; i++)
		{
			int rgb = Color.getRandomColorImproved(rand).asInt();
			assertTrue(rgb >= 0 && rgb <= 0xFFFFFF, "color " + Integer.toHexString(rgb));
		}
	}
}
