package fr.minecraftpp.core.set;

/**
 * Food values of an edible item. One nutrition point is half a drumstick.
 */
public record FoodDefinition(int nutrition, float saturation, boolean wolfFood, boolean alwaysEdible)
{
	public FoodDefinition
	{
		Bounds.check("nutrition", nutrition, 1, Integer.MAX_VALUE);
		Bounds.check("saturation", saturation, 0, Float.MAX_VALUE);
	}
}
