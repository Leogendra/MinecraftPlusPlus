package fr.minecraftpp.core.set.material;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import fr.minecraftpp.core.set.Bounds;

/**
 * Statistics shared by the five tools of a material.
 *
 * @param durability  number of uses of each tool
 * @param efficiency  mining speed of the tools on the blocks they are made for
 * @param damageBonus attack damage added by the material to each tool
 * @param speedBonus  attack speed added by the material to each tool
 */
public record ToolStats(int durability, float efficiency, Map<ToolType, Float> damageBonus, Map<ToolType, Float> speedBonus)
{
	public ToolStats
	{
		Bounds.check("tool durability", durability, 1, Integer.MAX_VALUE);
		Bounds.check("tool efficiency", efficiency, 0, Float.MAX_VALUE);
		damageBonus = copyOfEveryTool(damageBonus, "damage bonus");
		speedBonus = copyOfEveryTool(speedBonus, "speed bonus");
	}

	/**
	 * The attack modifiers of a tool, as the 1.12 tool classes applied them: the base attack of the tool plus the material bonus. The sword speed is fixed at -2.4, the hoe damage at 0, and the hoe speed is its bonus minus 4.
	 */
	public ToolAttack attack(ToolType toolType)
	{
		float damage = this.damageBonus.get(toolType);
		float speed = this.speedBonus.get(toolType);

		return switch (toolType)
		{
			case SWORD -> new ToolAttack(3.0F + damage, -2.4F);
			case PICKAXE -> new ToolAttack(1.0F + damage, -2.8F + speed);
			case AXE -> new ToolAttack(damage, speed);
			case SHOVEL -> new ToolAttack(1.5F + damage, -3.0F + speed);
			case HOE -> new ToolAttack(0.0F, speed - 4.0F);
		};
	}

	private static Map<ToolType, Float> copyOfEveryTool(Map<ToolType, Float> values, String name)
	{
		EnumMap<ToolType, Float> copy = new EnumMap<>(ToolType.class);
		copy.putAll(values);

		if (copy.size() != ToolType.values().length)
		{
			throw new IllegalArgumentException("Every tool needs a " + name + ": " + copy.keySet());
		}
		else
		{
			return Collections.unmodifiableMap(copy);
		}
	}

	/**
	 * Attack modifiers of a tool: damage added to the player base damage, and attack speed added to the player base speed of 4 attacks per second.
	 */
	public record ToolAttack(float damage, float speed)
	{
	}
}
