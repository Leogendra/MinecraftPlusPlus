package fr.minecraftpp.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.client.world.WorldSeedTexts;
import fr.minecraftpp.world.MppSeedFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelSummary;

/**
 * Shows the Minecraft++ seed status of each world in the world list, as 1.12 did: valid, wrong, or vanilla.
 */
@Mixin(WorldSelectionList.WorldListEntry.class)
public abstract class WorldListEntryMixin
{
	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelSummary;getInfo()Lnet/minecraft/network/chat/Component;"))
	private Component minecraftpp$showSeedStatus(LevelSummary summary, Operation<Component> original)
	{
		MppSeedFile seedFile = new MppSeedFile(Minecraft.getInstance().getLevelSource().getLevelPath(summary.getLevelId()));

		return WorldSeedTexts.worldListInfo(seedFile.status(MinecraftPlusPlus.catalog().seed()), original.call(summary));
	}
}
