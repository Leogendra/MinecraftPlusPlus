package fr.minecraftpp.gametest;

import java.util.Arrays;
import java.util.List;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.pack.data.OreFeatureWriter;
import fr.minecraftpp.world.OreBiomeModifications;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * The game test world is flat, so the test reads what the overworld biomes would generate instead of digging a generated chunk.
 */
public class OreGenerationGameTest
{
	@GameTest
	public void overworldBiomesGenerateTheModOresInsteadOfTheVanillaOnes(GameTestHelper helper)
	{
		Registry<PlacedFeature> placedFeatures = helper.getLevel().registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
		Registry<Biome> biomes = helper.getLevel().registryAccess().lookupOrThrow(Registries.BIOME);

		for (ResourceKey<Biome> biomeKey : List.of(Biomes.PLAINS, Biomes.BADLANDS, Biomes.JAGGED_PEAKS))
		{
			BiomeGenerationSettings settings = biomes.getValueOrThrow(biomeKey).getGenerationSettings();

			for (OreSetDefinition set : MinecraftPlusPlus.catalog().sets())
			{
				PlacedFeature ore = placedFeatures.getValueOrThrow(ResourceKey.create(Registries.PLACED_FEATURE, OreFeatureWriter.featureId(set)));

				helper.assertTrue(settings.hasFeature(ore), biomeKey.identifier() + " does not generate the " + set.name() + " ore");
			}

			for (ResourceKey<PlacedFeature> vanillaOre : OreBiomeModifications.replacedVanillaOres())
			{
				placedFeatures.getOptional(vanillaOre).ifPresent(feature -> helper.assertFalse(settings.hasFeature(feature), biomeKey.identifier() + " still generates " + vanillaOre.identifier()));
			}
		}

		helper.succeed();
	}

	/**
	 * The flat game test world never fills a chunk from the terrain noise, so the test checks that the mixin removing the vanilla copper and iron veins is merged into the class that would.
	 */
	@GameTest
	public void terrainNoiseDoesNotPlaceTheVanillaOreVeins(GameTestHelper helper)
	{
		boolean disabled = Arrays.stream(NoiseChunk.class.getDeclaredMethods()).anyMatch(method -> method.getName().contains("minecraftpp$disableVanillaOreVeins"));

		helper.assertTrue(disabled, "the vanilla ore veins are still generated");
		helper.succeed();
	}
}
