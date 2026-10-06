package fr.minecraftpp.world;

import java.util.List;
import java.util.function.Predicate;

import fr.minecraftpp.content.ContentRegistrar;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.pack.data.OreFeatureWriter;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Replaces the overworld ores of 1.12 (coal, iron, gold, redstone, diamond, lapis and emerald) and copper by the generated ores, as 1.12 did (decision D6). The nether ores stay vanilla.
 */
public final class OreBiomeModifications
{
	/**
	 * The biome tag, rather than the biomes the overworld of the current world generates, so that every world type gets the same ores.
	 */
	private static final Predicate<BiomeSelectionContext> OVERWORLD = BiomeSelectors.tag(BiomeTags.IS_OVERWORLD);

	private static final List<ResourceKey<PlacedFeature>> REPLACED_VANILLA_ORES = List.of(OrePlacements.ORE_COAL_UPPER, OrePlacements.ORE_COAL_LOWER, OrePlacements.ORE_IRON_UPPER, OrePlacements.ORE_IRON_MIDDLE, OrePlacements.ORE_IRON_SMALL, OrePlacements.ORE_GOLD, OrePlacements.ORE_GOLD_LOWER, OrePlacements.ORE_GOLD_EXTRA, OrePlacements.ORE_REDSTONE, OrePlacements.ORE_REDSTONE_LOWER, OrePlacements.ORE_DIAMOND, OrePlacements.ORE_DIAMOND_MEDIUM, OrePlacements.ORE_DIAMOND_LARGE, OrePlacements.ORE_DIAMOND_BURIED, OrePlacements.ORE_LAPIS, OrePlacements.ORE_LAPIS_BURIED, OrePlacements.ORE_EMERALD, OrePlacements.ORE_COPPER, OrePlacements.ORE_COPPER_LARGE);

	private OreBiomeModifications()
	{
	}

	public static void register(OreCatalog catalog)
	{
		BiomeModifications.create(ContentRegistrar.identifier("replace_vanilla_ores")).add(ModificationPhase.REMOVALS, OVERWORLD, context ->
		{
			for (ResourceKey<PlacedFeature> vanillaOre : REPLACED_VANILLA_ORES)
			{
				context.getGenerationSettings().removeFeature(vanillaOre);
			}
		});

		for (OreSetDefinition set : catalog.sets())
		{
			BiomeModifications.addFeature(OVERWORLD, GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, OreFeatureWriter.featureId(set)));
		}
	}

	public static List<ResourceKey<PlacedFeature>> replacedVanillaOres()
	{
		return REPLACED_VANILLA_ORES;
	}
}
