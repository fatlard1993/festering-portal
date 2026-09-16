# Festering Portal

A Fabric mod that makes nether portals built with **Crying Obsidian** spread nether corruption to the surrounding overworld.

## Features

- **Crying Obsidian Portals**: Build nether portal frames using Crying Obsidian instead of (or mixed with) regular Obsidian
- **Organic Corruption Spread**: Corruption spreads slowly and organically, one block at a time, like grass spreading
- **Scalable Radius**: Each Crying Obsidian block in the frame, corners included, adds 64 blocks to the maximum corruption radius (a minimum-size frame holds up to 14, for a 64-896 block radius; larger frames hold more)
- **Block Transformations**: Over 200 block types transform into nether equivalents:
  - Grass/Dirt → Netherrack/Soul Soil
  - Stone → Basalt/Blackstone
  - Surface water → Lava (with containment walls), except within 3 blocks of anything lava could set alight, which stays water
  - Trees → Crimson/Warped stems
  - And many more...
- **Mob Corruption**: Mobs standing on corrupted ground may transform:
  - Pig → Zombified Piglin
  - Villager → Zombie Villager
  - Slime → Magma Cube
  - Horse → Skeleton Horse
- **State-Preserving Swaps**: Every swap keeps the properties the old and new block share (facing, half, axis, lit, connections), so stairs, doors, torches and panes stay put
- **Maturation System**: Nether blocks evolve over time - netherrack becomes nylium, magma blocks melt into lava
- **Depth Limit**: Corruption only spreads near the surface (configurable depth)
- **Entity Trigger**: Entities arriving through a portal within 10 blocks of a festering portal trigger a corruption burst (at most once every 5 seconds)
- **Overworld Only**: Only portals lit in the overworld fester

## Learning It

Crying obsidian in a lit frame corrupts sixty-four blocks of overworld per stone. Nobody finds that out by accident, and nobody should find it out beside their own house.

With [village-quests](https://github.com/fatlard1993/village-quests) installed, a cleric who trusts you a great deal admits they built a frame a long way out of town, stopped halfway, and have not been back. They hand you three obsidian, one crying stone and a flint and steel, tell you roughly which direction, and ask you to finish it and come back and describe what you saw.

They are not being generous with the materials. Four stones and exactly one of them crying is the smallest festering portal there is, and they are still not going themselves.

Optional and guarded: without village-quests the mod behaves exactly as before.

## Usage

1. Build a nether portal frame using **Crying Obsidian** (regular obsidian corners are fine)
2. Light the portal with Flint and Steel
3. Watch as the nether corruption slowly spreads outward
4. The more Crying Obsidian in the frame, the further the corruption will spread

## Configuration

Configuration options can be found in `config/festeringportal.json` (generated on first run):

- `spreadIntervalTicks`: How often corruption spreads (default: 60 ticks = 3 seconds; 20 was the original default and proved far too fast in play)
- `radiusPerCryingObsidian`: Blocks of radius per crying obsidian (default: 64)
- `maxDepthBelowSurface`: How deep below surface corruption can spread (default: 4)
- `corruptMobs`: Enable/disable mob corruption (default: true)
- `mobCorruptionChance`: Chance per spread cycle that mobs within 20 blocks of the portal are corrupted (default: 0.05)
- `transformWaterToLava`: Enable/disable turning water into contained lava (default: true)
- `maxPortalsPerTick`: How many festering portals spread per cycle; the rest take turns (default: 10)

## Development

Installing is in [DEVELOPMENT.md](DEVELOPMENT.md).

## License

MIT, see [LICENSE](LICENSE).
