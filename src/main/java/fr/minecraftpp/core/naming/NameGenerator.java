package fr.minecraftpp.core.naming;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

/**
 * Generates the ore names of a seed from a letter-transition dictionary (the content of wordGen.mpp).
 *
 * The names only depend on the seed: the generator draws from its own random source, separate from the one of the ore generation, as in 1.12.
 */
public class NameGenerator
{
	private static final Character[] ALPHABET = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private static final Character WORD_START = '$';
	private static final Character WORD_END = '#';

	private final Tree tree;

	public NameGenerator(long seed, BufferedReader dictionary) throws IOException
	{
		this.tree = new Tree(new Random(seed), ALPHABET, WORD_START, WORD_END);
		this.tree.buildFromFile(dictionary);
	}

	public String nextName()
	{
		return this.tree.makeWord().toString();
	}
}
