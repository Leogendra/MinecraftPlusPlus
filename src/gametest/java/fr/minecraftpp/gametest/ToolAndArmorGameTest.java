package fr.minecraftpp.gametest;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.MaterialDefinition;
import fr.minecraftpp.core.set.material.ToolType;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/**
 * ing (metal) and brumed (material) are the equipped sets of the game test seed.
 */
public class ToolAndArmorGameTest
{
	@GameTest
	public void toolsHaveTheGeneratedStatistics(GameTestHelper helper)
	{
		for (String setName : new String[] { "ing", "brumed" })
		{
			OreSetDefinition set = GameTestSets.set(setName);
			MaterialDefinition material = set.material().orElseThrow();

			for (ToolType toolType : ToolType.values())
			{
				ItemStack tool = new ItemStack(MinecraftPlusPlus.content().item(ContentIds.tool(set, toolType)));

				helper.assertTrue(tool.getMaxDamage() == material.tools().durability(), setName + " " + toolType + " durability " + tool.getMaxDamage());
				helper.assertTrue(modifier(tool, Attributes.ATTACK_DAMAGE) == material.tools().attack(toolType).damage(), setName + " " + toolType + " attack damage " + modifier(tool, Attributes.ATTACK_DAMAGE));
				helper.assertTrue(modifier(tool, Attributes.ATTACK_SPEED) == material.tools().attack(toolType).speed(), setName + " " + toolType + " attack speed " + modifier(tool, Attributes.ATTACK_SPEED));
			}
		}

		helper.succeed();
	}

	@GameTest
	public void armorHasTheGeneratedProtection(GameTestHelper helper)
	{
		OreSetDefinition set = GameTestSets.set("ing");
		MaterialDefinition material = set.material().orElseThrow();

		for (ArmorPiece piece : ArmorPiece.values())
		{
			ItemStack armor = new ItemStack(MinecraftPlusPlus.content().item(ContentIds.armor(set, piece)));

			helper.assertTrue(armor.getMaxDamage() == material.armor().durability(piece), piece + " durability " + armor.getMaxDamage());
			helper.assertTrue(modifier(armor, Attributes.ARMOR) == material.armor().defense(piece), piece + " defense " + modifier(armor, Attributes.ARMOR));
			helper.assertTrue(modifier(armor, Attributes.ARMOR_TOUGHNESS) == material.armor().toughness(), piece + " toughness " + modifier(armor, Attributes.ARMOR_TOUGHNESS));
		}

		helper.succeed();
	}

	private static float modifier(ItemStack stack, Holder<Attribute> attribute)
	{
		ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

		return (float) modifiers.modifiers().stream().filter(entry -> entry.attribute().equals(attribute)).mapToDouble(entry -> entry.modifier().amount()).sum();
	}
}
