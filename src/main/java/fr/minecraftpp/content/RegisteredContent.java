package fr.minecraftpp.content;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * The blocks and items registered for the generated sets, by identifier path (the namespace is always minecraftpp).
 */
public class RegisteredContent
{
	private final Map<String, Block> blocks = new LinkedHashMap<>();
	private final Map<String, Item> items = new LinkedHashMap<>();

	void addBlock(String path, Block block)
	{
		this.blocks.put(path, block);
	}

	void addItem(String path, Item item)
	{
		this.items.put(path, item);
	}

	public Block block(String path)
	{
		Block block = this.blocks.get(path);

		if (block == null)
		{
			throw new IllegalArgumentException("No generated block " + path);
		}
		else
		{
			return block;
		}
	}

	public Item item(String path)
	{
		Item item = this.items.get(path);

		if (item == null)
		{
			throw new IllegalArgumentException("No generated item " + path);
		}
		else
		{
			return item;
		}
	}

	public Collection<Block> blocks()
	{
		return Collections.unmodifiableCollection(this.blocks.values());
	}

	public Collection<Item> items()
	{
		return Collections.unmodifiableCollection(this.items.values());
	}
}
