package fr.minecraftpp.pack.asset;

import java.util.List;

/**
 * An item definition ({@code assets/<namespace>/items/<item>.json}): the model drawn for the item, with the tint of each tinted layer.
 */
public record ItemDefinitionJson(ItemDefinitionJson.TintedModel model)
{
	private static final String MODEL_TYPE = "minecraft:model";
	private static final String CONSTANT_TINT_TYPE = "minecraft:constant";

	/**
	 * The model with a single tint, applied to its layer or faces of tint index 0.
	 *
	 * @param rgb the tint color, without alpha
	 */
	public static ItemDefinitionJson tinted(String model, int rgb)
	{
		return new ItemDefinitionJson(new TintedModel(MODEL_TYPE, model, List.of(new ConstantTint(CONSTANT_TINT_TYPE, rgb))));
	}

	public record TintedModel(String type, String model, List<ConstantTint> tints)
	{
	}

	public record ConstantTint(String type, int value)
	{
	}
}
