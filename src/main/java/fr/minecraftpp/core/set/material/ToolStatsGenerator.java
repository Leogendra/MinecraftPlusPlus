package fr.minecraftpp.core.set.material;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * Draws the tool statistics of a material, with the formulas and the draw order of the 1.12 MaterialSet.
 */
public final class ToolStatsGenerator
{
	private ToolStatsGenerator()
	{
	}

	public static ToolStats generate(Random rand, int tier)
	{
		int durability = generateDurability(rand, tier);
		float efficiency = generateEfficiency(rand, tier);
		Map<ToolType, Float> damageBonus = generateDamageBonus(rand, tier);
		Map<ToolType, Float> speedBonus = generateSpeedBonus(rand, tier);

		return new ToolStats(durability, efficiency, damageBonus, speedBonus);
	}

	private static int generateDurability(Random rand, int tier)
	{
		double power = (rand.nextDouble() * rand.nextInt((tier * 3) + 2)) + 4;
		return (int) Math.round(Math.pow(2, power));
	}

	private static float generateEfficiency(Random rand, int tier)
	{
		return (rand.nextFloat() * rand.nextInt((tier * 5) + 2)) + 4;
	}

	private static Map<ToolType, Float> generateDamageBonus(Random rand, int tier)
	{
		float gradient = rand.nextInt(3) + tier;
		Map<ToolType, Float> bonus = new EnumMap<>(ToolType.class);

		bonus.put(ToolType.SWORD, gradient);
		bonus.put(ToolType.PICKAXE, gradient);
		bonus.put(ToolType.AXE, 6.0F + tier);
		bonus.put(ToolType.SHOVEL, gradient);
		bonus.put(ToolType.HOE, 0.0F);

		return bonus;
	}

	/**
	 * Only the axe has a speed bonus. The 1.12 formula divides integers, so the drawn value is lost and the bonus is always -3.2.
	 */
	private static Map<ToolType, Float> generateSpeedBonus(Random rand, int tier)
	{
		float axeSpeed = (rand.nextInt(tier + 3) / 10) - 3.2F;
		Map<ToolType, Float> bonus = new EnumMap<>(ToolType.class);

		bonus.put(ToolType.SWORD, 0.0F);
		bonus.put(ToolType.PICKAXE, 0.0F);
		bonus.put(ToolType.AXE, axeSpeed);
		bonus.put(ToolType.SHOVEL, 0.0F);
		bonus.put(ToolType.HOE, 0.0F);

		return bonus;
	}
}
