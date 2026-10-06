package fr.minecraftpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class MinecraftPlusPlus implements ModInitializer
{
	public static final String MOD_ID = "minecraftpp";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Minecraft++ loaded");
	}
}
