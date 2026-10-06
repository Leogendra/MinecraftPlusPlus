package fr.minecraftpp.core.variant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.TestCatalogs;

/**
 * Seed 42: cychotinte (metal, gold), kal (material, iron), rize (material, diamond), derepess (simple, coal).
 */
class VariantCatalogTest
{
	private static final VariantCatalog VARIANTS = VariantCatalog.of(TestCatalogs.generate(42));

	@Test
	void listsTheIronVariants()
	{
		assertEquals(List.of("minecraftpp:kal"), VARIANTS.variantsOf("minecraft:iron_ingot"));
		assertEquals(List.of("minecraftpp:kal_block"), VARIANTS.variantsOf("minecraft:iron_block"));
		assertEquals(List.of("minecraftpp:kal_pickaxe"), VARIANTS.variantsOf("minecraft:iron_pickaxe"));
		assertEquals(List.of("minecraftpp:kal_chestplate"), VARIANTS.variantsOf("minecraft:iron_chestplate"));
	}

	/**
	 * The iron golem pattern reads the block variants; the items of a block are not blocks.
	 */
	@Test
	void onlyStorageBlocksAreBlockVariants()
	{
		assertEquals(List.of("minecraftpp:kal_block"), VARIANTS.blockVariantsOf("minecraft:iron_block"));
		assertFalse(VARIANTS.vanillaBlocks().contains("minecraft:iron_ingot"));
	}

	@Test
	void copperSetsReplaceTheVanillaCopper()
	{
		assertEquals(List.of("minecraftpp:dium"), VARIANTS.variantsOf("minecraft:copper_ingot"));
		assertEquals(List.of("minecraftpp:dium_block"), VARIANTS.blockVariantsOf("minecraft:copper_block"));
	}

	@Test
	void onlyMetalSetsReplaceNuggets()
	{
		assertEquals(List.of("minecraftpp:cychotinte_nugget"), VARIANTS.variantsOf("minecraft:gold_nugget"));
		assertFalse(VARIANTS.hasVariants("minecraft:iron_nugget"));
	}

	@Test
	void simpleSetsOnlyReplaceTheItemAndBlock()
	{
		assertEquals(List.of("minecraftpp:derepess"), VARIANTS.variantsOf("minecraft:coal"));
		assertEquals(List.of("minecraftpp:derepess_block"), VARIANTS.variantsOf("minecraft:coal_block"));
	}

	@Test
	void vanillaItemsWithoutRoleHaveNoVariant()
	{
		assertTrue(VARIANTS.variantsOf("minecraft:emerald").isEmpty());
		assertEquals(List.of("minecraftpp:rize_pickaxe"), VARIANTS.variantsOf("minecraft:diamond_pickaxe"));
	}
}
