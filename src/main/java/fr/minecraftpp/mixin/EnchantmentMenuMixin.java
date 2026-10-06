package fr.minecraftpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import fr.minecraftpp.content.ModTags;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;

/**
 * A shift-click moves the generated enchanting currency, instead of lapis lazuli, into the currency slot of the enchanting table.
 */
@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin
{
	/**
	 * The vanilla check is {@code stack.is(Items.LAPIS_LAZULI)}, compiled as the generic {@code TypedInstance.is(T)}, hence the {@code Object} parameter.
	 */
	@WrapOperation(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
	private boolean minecraftpp$moveEnchantingCurrency(ItemStack stack, Object lapisLazuli, Operation<Boolean> original)
	{
		return stack.is(ModTags.ENCHANTING_CURRENCY);
	}
}
