package fr.minecraftpp.pack.asset;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * An equipment asset: for each body layer type, the textures drawn on the entity wearing the armor, from bottom to top. A null field is left out of the file.
 */
public record EquipmentAssetJson(Map<String, List<EquipmentAssetJson.Layer>> layers)
{
	/**
	 * @param dyeable the tint of the layer, or null for a layer drawn as is
	 */
	public record Layer(String texture, Dyeable dyeable)
	{
	}

	/**
	 * @param colorWhenUndyed the tint used while the armor is not dyed, which the generated armor never is
	 */
	public record Dyeable(@SerializedName("color_when_undyed") int colorWhenUndyed)
	{
	}
}
