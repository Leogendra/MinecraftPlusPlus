package fr.minecraftpp.client;

import fr.minecraftpp.MinecraftPlusPlus;
import net.fabricmc.api.ClientModInitializer;

/**
 * Client entry point, run after the common entry point: plugs the client-only modules into the game.
 */
public class MinecraftPlusPlusClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		DynamicBlockTints.register(MinecraftPlusPlus.catalog(), MinecraftPlusPlus.content());
	}
}
