# Festering Portal

A Fabric mod for Minecraft 1.21.11 that makes nether portals built with **Crying Obsidian** spread nether corruption to the surrounding overworld.

## Features

- **Crying Obsidian Portals**: Build nether portal frames using Crying Obsidian instead of (or mixed with) regular Obsidian
- **Organic Corruption Spread**: Corruption spreads slowly and organically, one block at a time, like grass spreading
- **Scalable Radius**: Each Crying Obsidian block in the frame adds 64 blocks to the maximum corruption radius (1-14 blocks = 64-896 block radius)
- **Block Transformations**: Over 200 block types transform into nether equivalents:
  - Grass/Dirt → Netherrack/Soul Soil
  - Stone → Basalt/Blackstone
  - Water → Lava (with containment walls)
  - Trees → Crimson/Warped stems
  - And many more...
- **Mob Corruption**: Mobs standing on corrupted ground may transform:
  - Pig → Zombified Piglin
  - Villager → Zombie Villager
  - Slime → Magma Cube
  - Horse → Skeleton Horse
- **Maturation System**: Nether blocks evolve over time - netherrack becomes nylium, magma blocks melt into lava
- **Depth Limit**: Corruption only spreads near the surface (configurable depth)
- **Entity Trigger**: Entities exiting the portal trigger corruption bursts

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.11
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download the latest release and place in your `mods` folder

## Usage

1. Build a nether portal frame using **Crying Obsidian** (regular obsidian corners are fine)
2. Light the portal with Flint and Steel
3. Watch as the nether corruption slowly spreads outward
4. The more Crying Obsidian in the frame, the further the corruption will spread

## Configuration

Configuration options can be found in the config file (generated on first run):

- `spreadIntervalTicks`: How often corruption spreads (default: 20 ticks = 1 second)
- `radiusPerCryingObsidian`: Blocks of radius per crying obsidian (default: 64)
- `maxDepthBelowSurface`: How deep below surface corruption can spread (default: 4)
- `corruptMobs`: Enable/disable mob corruption (default: true)
- `mobCorruptionChance`: Chance per tick for mob corruption (default: 0.05)

## Screenshots

*Screenshots coming soon*

## Building from Source

```bash
./gradlew build
```

The built jar will be in `build/libs/`.

## License

MIT License - see [LICENSE](LICENSE) for details.
