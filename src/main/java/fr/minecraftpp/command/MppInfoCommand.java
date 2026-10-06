package fr.minecraftpp.command;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;

import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.text.SetInfoFormatter;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/**
 * The /mppinfo command: lists the generated ores of the current seed, one line per set. It needs the commands to be enabled (permission level 2).
 */
public final class MppInfoCommand
{
	public static final String NAME = "mppinfo";

	private MppInfoCommand()
	{
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, OreCatalog catalog)
	{
		dispatcher.register(Commands.literal(NAME).requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)).executes(context -> execute(context.getSource(), catalog)));
	}

	/**
	 * @return the number of listed sets, as the command result
	 */
	private static int execute(CommandSourceStack source, OreCatalog catalog)
	{
		List<String> lines = SetInfoFormatter.format(catalog).lines().toList();

		for (String line : lines)
		{
			source.sendSuccess(() -> Component.literal(line), false);
		}

		return lines.size();
	}
}
