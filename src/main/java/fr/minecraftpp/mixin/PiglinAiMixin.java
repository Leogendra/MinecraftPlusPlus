package fr.minecraftpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import fr.minecraftpp.content.ModTags;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;

/**
 * The piglins barter the generated gold as the vanilla gold ingot (decision D7): picked up, handed over or held by a player.
 */
@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin
{
	/**
	 * The vanilla check is {@code stack.is(BARTERING_ITEM)}, compiled as the generic {@code TypedInstance.is(T)}, hence the {@code Object} parameter.
	 */
	@WrapOperation(method = "isBarterCurrency", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
	private static boolean minecraftpp$barterGoldVariants(ItemStack stack, Object goldIngot, Operation<Boolean> original)
	{
		return original.call(stack, goldIngot) || stack.is(ModTags.GOLD_INGOT_VARIANTS);
	}
}
