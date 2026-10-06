package fr.minecraftpp.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.pack.GeneratedPackSource;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;

/**
 * The server data packs and the client resource packs both start from a {@link BuiltInPackSource}: the generated pack is added next to the vanilla pack, with the type of the source.
 */
@Mixin(BuiltInPackSource.class)
public abstract class BuiltInPackSourceMixin
{
	@Shadow
	@Final
	private PackType packType;

	@Inject(method = "loadPacks", at = @At("RETURN"))
	private void minecraftpp$addGeneratedPack(Consumer<Pack> result, CallbackInfo callbackInfo)
	{
		new GeneratedPackSource(this.packType, MinecraftPlusPlus.packContents()).loadPacks(result);
	}
}
