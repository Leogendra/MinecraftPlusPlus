package fr.minecraftpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

/**
 * The large copper and iron veins are vanilla ores placed by the terrain noise rather than by a biome feature, so the biome modifications cannot remove them (decision D6). The noise settings are left untouched: only the terrain filling ignores them.
 */
@Mixin(NoiseChunk.class)
public abstract class NoiseChunkMixin
{
	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;oreVeinsEnabled()Z"))
	private boolean minecraftpp$disableVanillaOreVeins(NoiseGeneratorSettings settings, Operation<Boolean> original)
	{
		return false;
	}
}
