# Golden fixtures of the 1.12 generator

Each `seed-<seed>.txt` file is the complete output of the Minecraft 1.12 version of the mod for one Minecraft++ seed. The tests of the `core` package regenerate the same content from the same seed and compare it with these files, line by line, to prove that the port keeps the 1.12 behavior.

**These files must never be edited by hand.** A difference between a file and the generator output is a regression of the port, not a reason to update the file.

## Content

- `# info`: the text of the 1.12 `/mppinfo` command (`SetManager.getInfoString()`).
- `# details`: every generated value of each set, read from the 1.12 objects: roles, ore rarity, textures, color, item and block traits, ore drops and experience, material statistics. Floats are printed with `Float.toString`.
- `# translations`: the display names registered by the 1.12 `ModLanguage`.

## Seeds

The four seeds were chosen among 43 captured seeds because, together, they cover every set type (simple, material, metal) and every random trait (shiny, fire starter, food, wolf food, falling, glowing, opacity, absorbing, slipperiness, acceleration, walk damage, flammability).

## How the files were produced

The files were produced from the tag `v1.12-final`, without changing the 1.12 code:

1. A small capture program, not committed, was compiled together with the MCP tree `src/minecraft/` (JDK 17, `--release 8`) against the Minecraft 1.12 libraries.
2. It was run on a Java 8 runtime, because the 1.12 constraint solver evaluates its constraints with the Nashorn JavaScript engine, removed from Java 15. The working directory held `MppConfig.mpp` (`seed=<seed>`) and `jars/wordGen.mpp`.
3. The program calls `Bootstrap.register()`, which runs the mod generation exactly as the game does, then prints the three sections above, reading the private fields of the 1.12 objects by reflection.

Two runs with the same seed produce identical files.
