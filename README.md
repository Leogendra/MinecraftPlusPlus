# Minecraft++

Minecraft++ is a modification of **Minecraft Java 1.12**, made with [MCP](https://minecraft.wiki/w/Tutorial:Programs_and_editors/Mod_Coder_Pack) that replaces the vanilla ores with **7 procedurally generated ores**. Each ore has:

- A name and a color
- A rarity and a distribution in the world
- One or more roles taken from the vanilla ores (coal, iron, gold, redstone, diamond, lapis, emerald…)
- Sometimes special properties (glowing, flammable, etc.)
- Its own tools, armor and recipes.

## The two seeds

| Seed | Where | Role |
|---|---|---|
| **World seed** | "Create New World" screen, as in vanilla | Defines the terrain like usual. |
| **Minecraft++ seed** | `MppConfig.mpp` in the game folder (`%APPDATA%\.minecraft`), in the format `seed=<number>` | Defines the 7 ores. The file is created with a random value on first launch, and is only read when the game starts. |

Each world stores the Minecraft++ seed it was created with in `saves/<world>/mppSeed.mpp`. A world only opens if this value matches the one in `MppConfig.mpp`. The world list shows `Valid Mpp Seed` or `Wrong Mpp Seed`.

- To play a world created with another seed: copy the first line of its `mppSeed.mpp` into `MppConfig.mpp` (`seed=<value>`), then restart the game. Never edit `mppSeed.mpp`.
- Vanilla worlds (without `mppSeed.mpp`) cannot be opened with the mod.
- The `/mppinfo` command (cheats enabled) lists the ores of the current seed.

## Installation (Windows)

The repository only contains the source code: you need to compile it with MCP 9.40, then add it as a version in your launcher. This procedure was tested on Windows 11 with MCP 9.40 and Temurin JDK 8.

### Requirements

- Minecraft Java **1.12** (not 1.12.2), installed and launched at least once from the launcher.
- A **JDK 8**, for example [Eclipse Temurin 8](https://adoptium.net/temurin/releases/?version=8). Note its installation folder, for example `C:\Program Files\Eclipse Adoptium\jdk-8.0.xxx-hotspot`.

Run all the following commands in **one and the same PowerShell window**, without closing it between steps.

### 1. Set the paths

Adapt the first two lines:

```powershell
$JDK  = "C:\Program Files\Eclipse Adoptium\jdk-8.0.xxx-hotspot"  # JDK 8 folder
$REPO = "C:\path\to\MinecraftPlusPlus"                           # this repository
$MCP  = "C:\mcp940"                                              # keep a short path
$MC   = "$env:APPDATA\.minecraft"
```

### 2. Compile the mod with MCP 9.40

```powershell
Invoke-WebRequest http://www.modcoderpack.com/files/mcp940.zip -OutFile "$env:TEMP\mcp940.zip"
Expand-Archive "$env:TEMP\mcp940.zip" $MCP
$env:PATH = "$JDK\bin;$env:PATH"
Set-Location $MCP
.\decompile.bat
```

If the download link no longer works, MCP 9.40 is also available on [MCP-Archive](https://github.com/GNU-Pattor-Team/MCP-Archive/tree/master/v9.40). It is a `.7z` archive, to extract with 7-Zip into `C:\mcp940`.

The `$env:PATH` line is required: MCP 9.40 is meant for Java 8, the only version tested, and uses the first `javac` found in the PATH. Without this line, it would pick another Java version installed on the machine. `decompile.bat` copies Minecraft 1.12 from `.minecraft` and decompiles it, which takes a few minutes. Like the other MCP scripts, it ends with "Press any key to continue".

Then replace the vanilla sources with the ones from this repository, recompile and reobfuscate:

```powershell
Remove-Item -Recurse -Force src\minecraft
Copy-Item -Recurse "$REPO\src\minecraft" src\minecraft
.\recompile.bat
.\reobfuscate.bat
```

The mod's classes are now in `C:\mcp940\reobf\minecraft`.

### 3. Create the version in the launcher

```powershell
$OUT = "$MC\versions\MinecraftPlusPlus"
$TMP = "$env:TEMP\mpp-jar"
Remove-Item -Recurse -Force $TMP -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force $TMP, $OUT | Out-Null
Push-Location $TMP
& "$JDK\bin\jar.exe" xf "$MC\versions\1.12\1.12.jar"
Remove-Item -Recurse -Force META-INF
Copy-Item -Recurse -Force "$MCP\reobf\minecraft\*" .
Copy-Item -Recurse -Force "$REPO\assets\*" .\assets\
& "$JDK\bin\jar.exe" cfM "$OUT\MinecraftPlusPlus.jar" .
Pop-Location
Remove-Item -Recurse -Force $TMP

$json = Get-Content "$MC\versions\1.12\1.12.json" -Raw | ConvertFrom-Json
$json.id = "MinecraftPlusPlus"
$json.downloads.PSObject.Properties.Remove("client")
$json | ConvertTo-Json -Depth 100 | Set-Content -Encoding ASCII "$OUT\MinecraftPlusPlus.json"
Copy-Item "$REPO\jars\wordGen.mpp" $MC
```

This script does three things:

- It creates `versions\MinecraftPlusPlus\MinecraftPlusPlus.jar`: the vanilla 1.12 jar without its `META-INF` folder, with the classes from `reobf\minecraft` and the textures from the repository's `assets` folder added.
- It creates `MinecraftPlusPlus.json`, a copy of `1.12.json` with the new `id` and without the client download. Without this removal, the launcher would put the vanilla jar back.
- It copies `jars\wordGen.mpp` into `.minecraft`. This dictionary for the name generator is **required**.

### 4. Play

1. In the launcher, select the `MinecraftPlusPlus` version. With the official launcher, you first need to create an installation for this version.
2. On first launch, `MppConfig.mpp` is created with a random seed. To choose the seed, for example to get the same ores as a friend: close the game, change the value, then restart.
3. Go to Singleplayer, then "Create New World", as in vanilla.

## Troubleshooting

- **`ERROR : You should run the launcher at least once before starting MCP`**: launch vanilla Minecraft 1.12 once from the launcher.
- **`Error copying library …` during `decompile.bat`**: the path is too long. Move MCP to a short folder, for example `C:\mcp940`.
- **A world does not open (nothing happens)**: the Minecraft++ seed is different, see [The two seeds](#the-two-seeds).

## Acknowledgements

Thanks to Lluisaac and louis-parent.