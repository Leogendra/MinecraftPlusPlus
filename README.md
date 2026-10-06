# Minecraft++

Minecraft++ is a [Fabric](https://fabricmc.net/) mod for **Minecraft Java 26.1.2** that replaces the vanilla ores with **7 procedurally generated ores**. Each ore has:

- A name and a color
- A rarity and a distribution in the world, in stone and in deepslate
- One or more roles taken from the vanilla ores (coal, iron, gold, copper, redstone, diamond, lapis, emerald…): its items replace the vanilla ones in recipes, loot, villager trades, the enchanting table, the beacon, iron golems and piglin bartering
- Sometimes special traits (glowing, flammable, edible, slippery, harmful to walk on, etc.)
- For some of them, their own tools and armor.

The same Minecraft++ seed always gives the same 7 ores, so players can share a seed to play with the same ores.

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) **0.19.5** or newer for Minecraft **26.1.2**.
2. Put [Fabric API](https://modrinth.com/mod/fabric-api) for 26.1.2 and the Minecraft++ jar in the `mods` folder of the game (`%APPDATA%\.minecraft\mods` on Windows).
3. Launch the game with the Fabric profile.

Minecraft 26.1.2 requires Java 25, which the launcher provides.

## The two seeds

| Seed | Where | Role |
|---|---|---|
| **World seed** | "Create New World" screen, as in vanilla | Defines the terrain, as usual. |
| **Minecraft++ seed** | `config/minecraftpp/MppConfig.mpp` in the game folder, in the format `seed=<number>` | Defines the 7 ores. The file is created with a random seed on the first launch, and is only read when the game starts. |

Each world stores the Minecraft++ seed it was created with in `saves/<world>/mppSeed.mpp`. A world only opens if this seed matches the one in `MppConfig.mpp`; otherwise the game explains which seed the world needs. The world list shows `Valid Mpp Seed` or `Wrong Mpp Seed` for each world. Never edit `mppSeed.mpp`.

Worlds created without the mod show a warning and cannot be opened with it: they would not be playable in vanilla anymore.

## Examples

### Play with the ores of a friend

Your friend reads the first line of `saves/<world>/mppSeed.mpp` in their world, for example `-7046029254386353131`. Close the game, write this line in `config/minecraftpp/MppConfig.mpp`:

```text
seed=-7046029254386353131
```

then restart the game and create a new world: it has the same 7 ores as the world of your friend. A world your friend sends you opens the same way, with the seed of its own `mppSeed.mpp` file.

### List the ores of the current seed

In a world with cheats enabled, run:

```text
/mppinfo
```

The command lists the 7 ores of the seed: their rarity, type, roles, traits, where they generate and the tool needed to mine them, for example:

```text
voging : Common Simple; beacon, coal; food{amount=2, saturation=1.796238}, slipperiness{-0.19999999}, walk damage{1.0}; [level: 128, frequency: 14-20], needs wood to harvest
```

The game log also lists them when the game starts.

## Building from the sources

The build uses Gradle and [Fabric Loom](https://github.com/FabricMC/fabric-loom), and needs a **JDK 25** or newer:

```shell
./gradlew build
```

The mod jar is written to `build/libs/`. The build also runs the tests:

- `./gradlew test` runs the unit tests: the ore generator gives exactly the ores of the 1.12 version for the same seed, and the generated resources match their reference files.
- `./gradlew runGameTest` starts a game server and runs the game tests, with the seed `-7046029254386353131`.
- `./gradlew runClient` starts the game with the mod, to test it by hand.

The design of the port, step by step, is described in [MIGRATION.md](MIGRATION.md). The original Minecraft 1.12 version, built with MCP, is available from the `v1.12-final` tag.

## License

Minecraft++ is distributed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0), see the `LICENSE` file.

## Acknowledgements

Thanks to Lluisaac and louis-parent.
