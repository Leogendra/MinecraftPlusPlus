package fr.minecraftpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import fr.minecraftpp.content.ModTags;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.item.ItemStack;

/**
 * The iron golem is repaired with the generated iron as with the vanilla iron ingot (decision D7).
 */
@Mixin(IronGolem.class)
public abstract class IronGolemMixin
{
	/**
	 * The vanilla check is {@code stack.is(Items.IRON_INGOT)}, compiled as the generic {@code TypedInstance.is(T)}, hence the {@code Object} parameter.
	 */
	@WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
	private boolean minecraftpp$repairWithIronVariants(ItemStack stack, Object ironIngot, Operation<Boolean> original)
	{
		return original.call(stack, ironIngot) || stack.is(ModTags.IRON_INGOT_VARIANTS);
	}
}
