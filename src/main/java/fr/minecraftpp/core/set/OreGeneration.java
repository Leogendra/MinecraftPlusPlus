package fr.minecraftpp.core.set;

/**
 * Where and how often the ore generates, in the 1.12 terms: veins per chunk, blocks per vein, and maximum height in a world from 0 to 128.
 */
public record OreGeneration(int veinAmount, int veinDensity, int maxHeight)
{
	public static final int MAX_VEIN_AMOUNT = 20;
	public static final int MAX_VEIN_DENSITY = 20;
	public static final int MAX_HEIGHT = 128;

	public OreGeneration
	{
		Bounds.check("vein amount", veinAmount, 1, MAX_VEIN_AMOUNT);
		Bounds.check("vein density", veinDensity, 1, MAX_VEIN_DENSITY);
		Bounds.check("max height", maxHeight, 1, MAX_HEIGHT);
	}
}
