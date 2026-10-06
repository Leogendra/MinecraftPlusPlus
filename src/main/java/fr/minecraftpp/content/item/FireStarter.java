package fr.minecraftpp.content.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Lights a fire on the clicked face, like flint and steel. Unlike flint and steel, the item is consumed, as in 1.12.
 */
public final class FireStarter
{
	private FireStarter()
	{
	}

	public static InteractionResult lightFire(UseOnContext context)
	{
		Level level = context.getLevel();
		Player player = context.getPlayer();
		BlockPos firePos = context.getClickedPos().relative(context.getClickedFace());

		if (!BaseFireBlock.canBePlacedAt(level, firePos, context.getHorizontalDirection()))
		{
			return InteractionResult.FAIL;
		}
		else
		{
			level.playSound(player, firePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
			level.setBlock(firePos, BaseFireBlock.getState(level, firePos), Block.UPDATE_ALL_IMMEDIATE);
			level.gameEvent(player, GameEvent.BLOCK_PLACE, firePos);

			ItemStack stack = context.getItemInHand();
			if (player instanceof ServerPlayer serverPlayer)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, firePos, stack);
			}
			stack.consume(1, player);

			return InteractionResult.SUCCESS;
		}
	}
}
