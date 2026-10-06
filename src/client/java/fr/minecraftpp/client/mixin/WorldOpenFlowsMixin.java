package fr.minecraftpp.client.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.client.world.WorldSeedTexts;
import fr.minecraftpp.core.world.WorldSeedStatus;
import fr.minecraftpp.world.MppSeedFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;

/**
 * Refuses to open a world created with another Minecraft++ seed or without the mod, as 1.12 did: its ores would change, or a vanilla world would no longer open in vanilla. The player is told what to do instead.
 */
@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin
{
	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(method = "openWorld", at = @At("HEAD"), cancellable = true)
	private void minecraftpp$refuseWorldOfAnotherSeed(String levelId, Runnable onCancel, CallbackInfo callbackInfo)
	{
		MppSeedFile seedFile = new MppSeedFile(this.minecraft.getLevelSource().getLevelPath(levelId));
		WorldSeedStatus status = seedFile.status(MinecraftPlusPlus.catalog().seed());

		if (!status.canBeOpened())
		{
			this.minecraft.setScreen(new AlertScreen(onCancel, WorldSeedTexts.refusalTitle(status), WorldSeedTexts.refusalMessage(status, seedFile)));
			callbackInfo.cancel();
		}
	}
}
