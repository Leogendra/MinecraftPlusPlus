package fr.minecraftpp.pack.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.pack.GeneratedFile;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

/**
 * Seed 42: cychotinte is a metal, kal and rize are materials; the other sets have no tools or armor.
 */
class RepairMaterialTagWriterTest
{
	@Test
	void equippedSetsAreRepairedWithTheirMainItem()
	{
		List<GeneratedFile> files = new RepairMaterialTagWriter().write(TestCatalogs.generate(42));

		assertEquals(List.of(tag("cychotinte", "cychotinte_ingot"), tag("kal", "kal"), tag("rize", "rize")), files);
	}

	private static GeneratedFile tag(String setName, String mainItem)
	{
		return new GeneratedFile(PackType.SERVER_DATA, Identifier.fromNamespaceAndPath("minecraftpp", "tags/item/" + setName + "_repair_materials.json"), "{\"values\":[\"minecraftpp:" + mainItem + "\"]}");
	}
}
