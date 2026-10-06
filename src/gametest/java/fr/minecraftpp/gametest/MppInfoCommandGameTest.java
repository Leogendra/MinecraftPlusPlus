package fr.minecraftpp.gametest;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import fr.minecraftpp.command.MppInfoCommand;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.MinecraftServer;

public class MppInfoCommandGameTest
{
	@GameTest
	public void commandListsTheSevenSets(GameTestHelper helper) throws CommandSyntaxException
	{
		MinecraftServer server = helper.getLevel().getServer();
		CommandSourceStack source = server.createCommandSourceStack();

		int listedSets = server.getCommands().getDispatcher().execute(MppInfoCommand.NAME, source);

		helper.assertTrue(listedSets == 7, "expected 7 sets, got " + listedSets);
		helper.succeed();
	}
}
