package fr.minecraftpp.content.block.behaviour;

/**
 * The block is a redstone power source, like the vanilla redstone block.
 */
public class RedstonePowerModule implements BlockBehaviourModule
{
	private final int power;

	public RedstonePowerModule(int power)
	{
		this.power = power;
	}

	@Override
	public int signal()
	{
		return this.power;
	}
}
