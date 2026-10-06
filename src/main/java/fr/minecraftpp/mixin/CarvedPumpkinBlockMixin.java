package fr.minecraftpp.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import fr.minecraftpp.content.ModTags;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;

/**
 * The iron golem pattern accepts the generated iron blocks as the vanilla iron block (decision D7), in its body and arms.
 */
@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin
{
	@Unique
	private static final char IRON_BLOCK_SYMBOL = '#';

	@WrapOperation(method = { "getOrCreateIronGolemBase", "getOrCreateIronGolemFull" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/pattern/BlockPatternBuilder;where(CLjava/util/function/Predicate;)Lnet/minecraft/world/level/block/state/pattern/BlockPatternBuilder;"))
	private BlockPatternBuilder minecraftpp$acceptIronBlockVariants(BlockPatternBuilder builder, char symbol, Predicate<BlockInWorld> predicate, Operation<BlockPatternBuilder> original)
	{
		if (symbol == IRON_BLOCK_SYMBOL)
		{
			return original.call(builder, symbol, predicate.or(BlockInWorld.hasState(state -> state.is(ModTags.IRON_BLOCK_VARIANTS))));
		}
		else
		{
			return original.call(builder, symbol, predicate);
		}
	}
}
