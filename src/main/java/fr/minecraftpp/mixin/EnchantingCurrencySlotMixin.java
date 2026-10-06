package fr.minecraftpp.mixin;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import fr.minecraftpp.content.ModTags;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/**
 * The lapis lazuli slot of the enchanting table, an anonymous class of {@code EnchantmentMenu}: it takes the generated enchanting currency instead of lapis lazuli, and shows no lapis silhouette when empty, as the 1.12 table texture did.
 */
@Mixin(targets = "net.minecraft.world.inventory.EnchantmentMenu$3")
public abstract class EnchantingCurrencySlotMixin
{
	/**
	 * The vanilla check is {@code stack.is(Items.LAPIS_LAZULI)}, compiled as the generic {@code TypedInstance.is(T)}, hence the {@code Object} parameter.
	 */
	@WrapOperation(method = "mayPlace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
	private boolean minecraftpp$acceptEnchantingCurrency(ItemStack stack, Object lapisLazuli, Operation<Boolean> original)
	{
		return stack.is(ModTags.ENCHANTING_CURRENCY);
	}

	@Inject(method = "getNoItemIcon", at = @At("HEAD"), cancellable = true)
	private void minecraftpp$hideLapisSilhouette(CallbackInfoReturnable<@Nullable Identifier> callbackInfo)
	{
		callbackInfo.setReturnValue(null);
	}
}
