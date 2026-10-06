package fr.minecraftpp.core.set.material;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.set.material.ToolStats.ToolAttack;

class ToolStatsGeneratorTest
{
	@Test
	void drawsTheSameStatisticsAsThe112MaterialSet()
	{
		for (long seed = 0; seed < 50; seed++)
		{
			for (int tier = 0; tier <= 2; tier++)
			{
				ToolStats stats = ToolStatsGenerator.generate(new Random(seed), tier);
				Random legacy = new Random(seed);

				// Verbatim formulas of MaterialSet.getGeneratedToolDurability, getGeneratedEfficiency, getGeneratedToolAttackDamage and getGeneratedToolAttackSpeed in 1.12
				double power = (legacy.nextDouble() * legacy.nextInt((tier * 3) + 2)) + 4;
				int durability = (int) Math.round(Math.pow(2, power));
				float efficiency = (legacy.nextFloat() * legacy.nextInt((tier * 5) + 2)) + 4;
				float gradient = legacy.nextInt(3) + tier;
				float axeSpeed = (legacy.nextInt(tier + 3) / 10) - 3.2F;

				assertEquals(durability, stats.durability());
				assertEquals(efficiency, stats.efficiency());
				assertEquals(gradient, stats.damageBonus().get(ToolType.SWORD));
				assertEquals(6.0F + tier, stats.damageBonus().get(ToolType.AXE));
				assertEquals(axeSpeed, stats.speedBonus().get(ToolType.AXE));
			}
		}
	}

	@Test
	void appliesTheBaseAttackOfEach112Tool()
	{
		ToolStats stats = ToolStatsGenerator.generate(new Random(3), 1);
		float gradient = stats.damageBonus().get(ToolType.SWORD);

		assertEquals(new ToolAttack(3.0F + gradient, -2.4F), stats.attack(ToolType.SWORD));
		assertEquals(new ToolAttack(1.0F + gradient, -2.8F), stats.attack(ToolType.PICKAXE));
		assertEquals(new ToolAttack(7.0F, -3.2F), stats.attack(ToolType.AXE));
		assertEquals(new ToolAttack(1.5F + gradient, -3.0F), stats.attack(ToolType.SHOVEL));
		assertEquals(new ToolAttack(0.0F, -4.0F), stats.attack(ToolType.HOE));
	}
}
