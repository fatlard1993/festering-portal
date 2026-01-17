package com.festeringportal.corruption;

import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Registry of block transformations for nether corruption.
 * Maps overworld blocks to their nether equivalents.
 */
public class BlockTransformations {

    // Simple block-to-block mappings
    private static final Map<Block, Block> SIMPLE_TRANSFORMATIONS = new HashMap<>();

    // State-preserving transformations (for blocks with directional properties)
    private static final Map<Block, Function<BlockState, BlockState>> STATE_TRANSFORMATIONS = new HashMap<>();

    static {
        // === TERRAIN ===
        register(Blocks.GRASS_BLOCK, Blocks.NETHERRACK);
        register(Blocks.MYCELIUM, Blocks.NETHERRACK);
        register(Blocks.PODZOL, Blocks.SOUL_SOIL);
        register(Blocks.DIRT, Blocks.SOUL_SOIL);
        register(Blocks.COARSE_DIRT, Blocks.SOUL_SOIL);
        register(Blocks.ROOTED_DIRT, Blocks.SOUL_SOIL);
        register(Blocks.DIRT_PATH, Blocks.SOUL_SOIL);
        register(Blocks.FARMLAND, Blocks.SOUL_SOIL);
        register(Blocks.MUD, Blocks.SOUL_SOIL);

        // === STONE ===
        register(Blocks.STONE, Blocks.BASALT);
        register(Blocks.COBBLESTONE, Blocks.BLACKSTONE);
        register(Blocks.MOSSY_COBBLESTONE, Blocks.BLACKSTONE);
        register(Blocks.ANDESITE, Blocks.BASALT);
        register(Blocks.DIORITE, Blocks.BASALT);
        register(Blocks.GRANITE, Blocks.BASALT);
        register(Blocks.CALCITE, Blocks.BASALT);
        register(Blocks.TUFF, Blocks.BASALT);
        register(Blocks.DRIPSTONE_BLOCK, Blocks.BASALT);

        // === DEEPSLATE ===
        register(Blocks.DEEPSLATE, Blocks.BLACKSTONE);
        register(Blocks.COBBLED_DEEPSLATE, Blocks.BLACKSTONE);
        register(Blocks.POLISHED_DEEPSLATE, Blocks.POLISHED_BLACKSTONE);
        register(Blocks.DEEPSLATE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
        register(Blocks.DEEPSLATE_TILES, Blocks.POLISHED_BLACKSTONE_BRICKS);
        register(Blocks.CHISELED_DEEPSLATE, Blocks.CHISELED_POLISHED_BLACKSTONE);

        // === SAND & GRAVEL ===
        register(Blocks.SAND, Blocks.SOUL_SAND);
        register(Blocks.RED_SAND, Blocks.SOUL_SAND);
        register(Blocks.GRAVEL, Blocks.SOUL_SAND);
        register(Blocks.SANDSTONE, Blocks.NETHER_BRICKS);
        register(Blocks.RED_SANDSTONE, Blocks.RED_NETHER_BRICKS);
        register(Blocks.CLAY, Blocks.SOUL_SAND);

        // === FLUIDS ===
        // Water is handled specially in SpreadingAlgorithm to create supported lava pools
        // Don't register direct water->lava here
        // register(Blocks.WATER, Blocks.LAVA);

        // === ICE ===
        register(Blocks.ICE, Blocks.MAGMA_BLOCK);
        register(Blocks.PACKED_ICE, Blocks.MAGMA_BLOCK);
        register(Blocks.BLUE_ICE, Blocks.MAGMA_BLOCK);
        register(Blocks.SNOW_BLOCK, Blocks.MAGMA_BLOCK);
        register(Blocks.SNOW, Blocks.AIR);
        register(Blocks.POWDER_SNOW, Blocks.LAVA);

        // === VEGETATION (small plants) ===
        // Ground-based plants convert to nether ground plants
        register(Blocks.SHORT_GRASS, Blocks.NETHER_SPROUTS);
        register(Blocks.TALL_GRASS, Blocks.CRIMSON_ROOTS);
        register(Blocks.FERN, Blocks.WARPED_ROOTS);
        register(Blocks.LARGE_FERN, Blocks.CRIMSON_ROOTS); // Not twisting vines (would float)
        register(Blocks.DEAD_BUSH, Blocks.CRIMSON_ROOTS);
        // Aquatic plants wither away in corruption
        register(Blocks.SEAGRASS, Blocks.AIR);
        register(Blocks.TALL_SEAGRASS, Blocks.AIR);
        register(Blocks.KELP, Blocks.AIR);
        register(Blocks.KELP_PLANT, Blocks.AIR);
        register(Blocks.LILY_PAD, Blocks.AIR);

        // === FLOWERS ===
        register(Blocks.DANDELION, Blocks.CRIMSON_FUNGUS);
        register(Blocks.POPPY, Blocks.CRIMSON_FUNGUS);
        register(Blocks.BLUE_ORCHID, Blocks.WARPED_FUNGUS);
        register(Blocks.ALLIUM, Blocks.CRIMSON_FUNGUS);
        register(Blocks.AZURE_BLUET, Blocks.WARPED_FUNGUS);
        register(Blocks.RED_TULIP, Blocks.CRIMSON_FUNGUS);
        register(Blocks.ORANGE_TULIP, Blocks.CRIMSON_FUNGUS);
        register(Blocks.WHITE_TULIP, Blocks.WARPED_FUNGUS);
        register(Blocks.PINK_TULIP, Blocks.CRIMSON_FUNGUS);
        register(Blocks.OXEYE_DAISY, Blocks.WARPED_FUNGUS);
        register(Blocks.CORNFLOWER, Blocks.WARPED_FUNGUS);
        register(Blocks.LILY_OF_THE_VALLEY, Blocks.WARPED_FUNGUS);
        register(Blocks.SUNFLOWER, Blocks.CRIMSON_FUNGUS);
        register(Blocks.LILAC, Blocks.CRIMSON_FUNGUS);
        register(Blocks.ROSE_BUSH, Blocks.CRIMSON_FUNGUS);
        register(Blocks.PEONY, Blocks.WARPED_FUNGUS);
        register(Blocks.WITHER_ROSE, Blocks.WARPED_FUNGUS);

        // === MUSHROOMS ===
        register(Blocks.BROWN_MUSHROOM, Blocks.CRIMSON_FUNGUS);
        register(Blocks.RED_MUSHROOM, Blocks.WARPED_FUNGUS);
        register(Blocks.BROWN_MUSHROOM_BLOCK, Blocks.NETHER_WART_BLOCK);
        register(Blocks.RED_MUSHROOM_BLOCK, Blocks.WARPED_WART_BLOCK);
        register(Blocks.MUSHROOM_STEM, Blocks.SHROOMLIGHT);

        // === LEAVES ===
        register(Blocks.OAK_LEAVES, Blocks.NETHER_WART_BLOCK);
        register(Blocks.SPRUCE_LEAVES, Blocks.NETHER_WART_BLOCK);
        register(Blocks.BIRCH_LEAVES, Blocks.WARPED_WART_BLOCK);
        register(Blocks.JUNGLE_LEAVES, Blocks.NETHER_WART_BLOCK);
        register(Blocks.ACACIA_LEAVES, Blocks.NETHER_WART_BLOCK);
        register(Blocks.DARK_OAK_LEAVES, Blocks.NETHER_WART_BLOCK);
        register(Blocks.MANGROVE_LEAVES, Blocks.NETHER_WART_BLOCK);
        register(Blocks.CHERRY_LEAVES, Blocks.WARPED_WART_BLOCK);
        register(Blocks.AZALEA_LEAVES, Blocks.NETHER_WART_BLOCK);
        register(Blocks.FLOWERING_AZALEA_LEAVES, Blocks.WARPED_WART_BLOCK);

        // === WOOD LOGS (preserve axis) ===
        registerLogTransformation(Blocks.OAK_LOG, Blocks.CRIMSON_STEM);
        registerLogTransformation(Blocks.SPRUCE_LOG, Blocks.CRIMSON_STEM);
        registerLogTransformation(Blocks.BIRCH_LOG, Blocks.WARPED_STEM);
        registerLogTransformation(Blocks.JUNGLE_LOG, Blocks.CRIMSON_STEM);
        registerLogTransformation(Blocks.ACACIA_LOG, Blocks.CRIMSON_STEM);
        registerLogTransformation(Blocks.DARK_OAK_LOG, Blocks.CRIMSON_STEM);
        registerLogTransformation(Blocks.MANGROVE_LOG, Blocks.CRIMSON_STEM);
        registerLogTransformation(Blocks.CHERRY_LOG, Blocks.WARPED_STEM);

        // Stripped logs
        registerLogTransformation(Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_CRIMSON_STEM);
        registerLogTransformation(Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_CRIMSON_STEM);
        registerLogTransformation(Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_WARPED_STEM);
        registerLogTransformation(Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_CRIMSON_STEM);
        registerLogTransformation(Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_CRIMSON_STEM);
        registerLogTransformation(Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_CRIMSON_STEM);
        registerLogTransformation(Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_CRIMSON_STEM);
        registerLogTransformation(Blocks.STRIPPED_CHERRY_LOG, Blocks.STRIPPED_WARPED_STEM);

        // Wood (bark all around)
        registerLogTransformation(Blocks.OAK_WOOD, Blocks.CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.SPRUCE_WOOD, Blocks.CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.BIRCH_WOOD, Blocks.WARPED_HYPHAE);
        registerLogTransformation(Blocks.JUNGLE_WOOD, Blocks.CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.ACACIA_WOOD, Blocks.CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.DARK_OAK_WOOD, Blocks.CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.MANGROVE_WOOD, Blocks.CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.CHERRY_WOOD, Blocks.WARPED_HYPHAE);

        // === PLANKS ===
        register(Blocks.OAK_PLANKS, Blocks.CRIMSON_PLANKS);
        register(Blocks.SPRUCE_PLANKS, Blocks.CRIMSON_PLANKS);
        register(Blocks.BIRCH_PLANKS, Blocks.WARPED_PLANKS);
        register(Blocks.JUNGLE_PLANKS, Blocks.CRIMSON_PLANKS);
        register(Blocks.ACACIA_PLANKS, Blocks.CRIMSON_PLANKS);
        register(Blocks.DARK_OAK_PLANKS, Blocks.CRIMSON_PLANKS);
        register(Blocks.MANGROVE_PLANKS, Blocks.CRIMSON_PLANKS);
        register(Blocks.CHERRY_PLANKS, Blocks.WARPED_PLANKS);
        register(Blocks.BAMBOO_PLANKS, Blocks.WARPED_PLANKS);

        // === ORES (to nether variants where applicable) ===
        register(Blocks.GOLD_ORE, Blocks.NETHER_GOLD_ORE);
        register(Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE);
        register(Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_QUARTZ_ORE); // Already nether
        register(Blocks.ANCIENT_DEBRIS, Blocks.ANCIENT_DEBRIS); // Keep as is

        // === DECORATIVE / LIGHTING ===
        register(Blocks.TORCH, Blocks.SOUL_TORCH);
        register(Blocks.WALL_TORCH, Blocks.SOUL_WALL_TORCH);
        register(Blocks.LANTERN, Blocks.SOUL_LANTERN);
        register(Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
        register(Blocks.GLOWSTONE, Blocks.SHROOMLIGHT);
        register(Blocks.SEA_LANTERN, Blocks.SHROOMLIGHT);

        // === VINES & CLIMBING PLANTS ===
        // Wall vines wither in corruption (can't guarantee ceiling attachment for weeping vines)
        register(Blocks.VINE, Blocks.AIR);
        // Cave vines also wither - no guarantee of proper support
        register(Blocks.CAVE_VINES, Blocks.AIR);
        register(Blocks.CAVE_VINES_PLANT, Blocks.AIR);
        // Glow lichen withers
        register(Blocks.GLOW_LICHEN, Blocks.AIR);

        // === TALL PLANTS (would float) ===
        // These plants need specific ground support - replace with AIR
        register(Blocks.SUGAR_CANE, Blocks.AIR);
        register(Blocks.BAMBOO, Blocks.AIR);
        register(Blocks.BAMBOO_SAPLING, Blocks.AIR);
        register(Blocks.CACTUS, Blocks.AIR);

        // === SAPLINGS ===
        register(Blocks.OAK_SAPLING, Blocks.CRIMSON_FUNGUS);
        register(Blocks.SPRUCE_SAPLING, Blocks.CRIMSON_FUNGUS);
        register(Blocks.BIRCH_SAPLING, Blocks.WARPED_FUNGUS);
        register(Blocks.JUNGLE_SAPLING, Blocks.CRIMSON_FUNGUS);
        register(Blocks.ACACIA_SAPLING, Blocks.CRIMSON_FUNGUS);
        register(Blocks.DARK_OAK_SAPLING, Blocks.CRIMSON_FUNGUS);
        register(Blocks.CHERRY_SAPLING, Blocks.WARPED_FUNGUS);
        register(Blocks.MANGROVE_PROPAGULE, Blocks.CRIMSON_FUNGUS);
        register(Blocks.AZALEA, Blocks.CRIMSON_FUNGUS);
        register(Blocks.FLOWERING_AZALEA, Blocks.WARPED_FUNGUS);

        // === JUNGLE-SPECIFIC ===
        register(Blocks.COCOA, Blocks.AIR); // Needs jungle log - would float

        // === CROPS & FARMLAND PLANTS ===
        register(Blocks.WHEAT, Blocks.NETHER_SPROUTS);
        register(Blocks.CARROTS, Blocks.CRIMSON_ROOTS);
        register(Blocks.POTATOES, Blocks.CRIMSON_ROOTS);
        register(Blocks.BEETROOTS, Blocks.CRIMSON_ROOTS);
        register(Blocks.MELON, Blocks.SHROOMLIGHT);
        register(Blocks.PUMPKIN, Blocks.SHROOMLIGHT);
        register(Blocks.CARVED_PUMPKIN, Blocks.SHROOMLIGHT);
        register(Blocks.JACK_O_LANTERN, Blocks.SHROOMLIGHT);
        register(Blocks.MELON_STEM, Blocks.CRIMSON_ROOTS);
        register(Blocks.PUMPKIN_STEM, Blocks.CRIMSON_ROOTS);
        register(Blocks.ATTACHED_MELON_STEM, Blocks.CRIMSON_ROOTS);
        register(Blocks.ATTACHED_PUMPKIN_STEM, Blocks.CRIMSON_ROOTS);
        register(Blocks.SWEET_BERRY_BUSH, Blocks.CRIMSON_ROOTS);
        register(Blocks.TORCHFLOWER, Blocks.CRIMSON_FUNGUS);
        register(Blocks.PITCHER_PLANT, Blocks.WARPED_FUNGUS);
        register(Blocks.PITCHER_CROP, Blocks.WARPED_ROOTS);
        register(Blocks.TORCHFLOWER_CROP, Blocks.CRIMSON_ROOTS);

        // === MISC ===
        register(Blocks.BONE_BLOCK, Blocks.BONE_BLOCK); // Keep as is (fits nether)
        register(Blocks.HAY_BLOCK, Blocks.NETHER_WART_BLOCK);
        register(Blocks.MOSS_BLOCK, Blocks.NETHER_WART_BLOCK);
        register(Blocks.MOSS_CARPET, Blocks.NETHER_SPROUTS);
        register(Blocks.COBWEB, Blocks.AIR);
        register(Blocks.BEE_NEST, Blocks.SHROOMLIGHT);
        register(Blocks.BEEHIVE, Blocks.SHROOMLIGHT);
        register(Blocks.HONEYCOMB_BLOCK, Blocks.SHROOMLIGHT);
        register(Blocks.HONEY_BLOCK, Blocks.MAGMA_BLOCK);

        // === MANGROVE ===
        register(Blocks.MANGROVE_ROOTS, Blocks.CRIMSON_ROOTS);
        register(Blocks.MUDDY_MANGROVE_ROOTS, Blocks.SOUL_SOIL);

        // === GLASS (corruption makes it opaque/dark) ===
        register(Blocks.GLASS, Blocks.TINTED_GLASS);
        register(Blocks.GLASS_PANE, Blocks.IRON_BARS); // Corroded to iron bars

        // === SLABS (preserve type: top/bottom/double) ===
        registerSlabTransformation(Blocks.OAK_SLAB, Blocks.CRIMSON_SLAB);
        registerSlabTransformation(Blocks.SPRUCE_SLAB, Blocks.CRIMSON_SLAB);
        registerSlabTransformation(Blocks.BIRCH_SLAB, Blocks.WARPED_SLAB);
        registerSlabTransformation(Blocks.JUNGLE_SLAB, Blocks.CRIMSON_SLAB);
        registerSlabTransformation(Blocks.ACACIA_SLAB, Blocks.CRIMSON_SLAB);
        registerSlabTransformation(Blocks.DARK_OAK_SLAB, Blocks.CRIMSON_SLAB);
        registerSlabTransformation(Blocks.MANGROVE_SLAB, Blocks.CRIMSON_SLAB);
        registerSlabTransformation(Blocks.CHERRY_SLAB, Blocks.WARPED_SLAB);
        registerSlabTransformation(Blocks.BAMBOO_SLAB, Blocks.WARPED_SLAB);
        registerSlabTransformation(Blocks.STONE_SLAB, Blocks.BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        registerSlabTransformation(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        registerSlabTransformation(Blocks.BRICK_SLAB, Blocks.NETHER_BRICK_SLAB);
        registerSlabTransformation(Blocks.SANDSTONE_SLAB, Blocks.NETHER_BRICK_SLAB);
        registerSlabTransformation(Blocks.RED_SANDSTONE_SLAB, Blocks.RED_NETHER_BRICK_SLAB);
        registerSlabTransformation(Blocks.ANDESITE_SLAB, Blocks.BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.DIORITE_SLAB, Blocks.BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.GRANITE_SLAB, Blocks.BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.POLISHED_ANDESITE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.POLISHED_DIORITE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.POLISHED_GRANITE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
        registerSlabTransformation(Blocks.DEEPSLATE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        registerSlabTransformation(Blocks.DEEPSLATE_TILE_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);

        // === STAIRS (preserve facing/half/shape) ===
        registerStairTransformation(Blocks.OAK_STAIRS, Blocks.CRIMSON_STAIRS);
        registerStairTransformation(Blocks.SPRUCE_STAIRS, Blocks.CRIMSON_STAIRS);
        registerStairTransformation(Blocks.BIRCH_STAIRS, Blocks.WARPED_STAIRS);
        registerStairTransformation(Blocks.JUNGLE_STAIRS, Blocks.CRIMSON_STAIRS);
        registerStairTransformation(Blocks.ACACIA_STAIRS, Blocks.CRIMSON_STAIRS);
        registerStairTransformation(Blocks.DARK_OAK_STAIRS, Blocks.CRIMSON_STAIRS);
        registerStairTransformation(Blocks.MANGROVE_STAIRS, Blocks.CRIMSON_STAIRS);
        registerStairTransformation(Blocks.CHERRY_STAIRS, Blocks.WARPED_STAIRS);
        registerStairTransformation(Blocks.BAMBOO_STAIRS, Blocks.WARPED_STAIRS);
        registerStairTransformation(Blocks.STONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        registerStairTransformation(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        registerStairTransformation(Blocks.BRICK_STAIRS, Blocks.NETHER_BRICK_STAIRS);
        registerStairTransformation(Blocks.SANDSTONE_STAIRS, Blocks.NETHER_BRICK_STAIRS);
        registerStairTransformation(Blocks.RED_SANDSTONE_STAIRS, Blocks.RED_NETHER_BRICK_STAIRS);
        registerStairTransformation(Blocks.ANDESITE_STAIRS, Blocks.BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.DIORITE_STAIRS, Blocks.BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.GRANITE_STAIRS, Blocks.BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.POLISHED_ANDESITE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.POLISHED_DIORITE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.POLISHED_GRANITE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.COBBLED_DEEPSLATE_STAIRS, Blocks.BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.POLISHED_DEEPSLATE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
        registerStairTransformation(Blocks.DEEPSLATE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        registerStairTransformation(Blocks.DEEPSLATE_TILE_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);

        // === WALLS ===
        register(Blocks.COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
        register(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
        register(Blocks.STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        register(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        register(Blocks.BRICK_WALL, Blocks.NETHER_BRICK_WALL);
        register(Blocks.SANDSTONE_WALL, Blocks.NETHER_BRICK_WALL);
        register(Blocks.RED_SANDSTONE_WALL, Blocks.RED_NETHER_BRICK_WALL);
        register(Blocks.ANDESITE_WALL, Blocks.BLACKSTONE_WALL);
        register(Blocks.DIORITE_WALL, Blocks.BLACKSTONE_WALL);
        register(Blocks.GRANITE_WALL, Blocks.BLACKSTONE_WALL);
        register(Blocks.COBBLED_DEEPSLATE_WALL, Blocks.BLACKSTONE_WALL);
        register(Blocks.POLISHED_DEEPSLATE_WALL, Blocks.POLISHED_BLACKSTONE_WALL);
        register(Blocks.DEEPSLATE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        register(Blocks.DEEPSLATE_TILE_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);

        // === FENCES ===
        register(Blocks.OAK_FENCE, Blocks.CRIMSON_FENCE);
        register(Blocks.SPRUCE_FENCE, Blocks.CRIMSON_FENCE);
        register(Blocks.BIRCH_FENCE, Blocks.WARPED_FENCE);
        register(Blocks.JUNGLE_FENCE, Blocks.CRIMSON_FENCE);
        register(Blocks.ACACIA_FENCE, Blocks.CRIMSON_FENCE);
        register(Blocks.DARK_OAK_FENCE, Blocks.CRIMSON_FENCE);
        register(Blocks.MANGROVE_FENCE, Blocks.CRIMSON_FENCE);
        register(Blocks.CHERRY_FENCE, Blocks.WARPED_FENCE);
        register(Blocks.BAMBOO_FENCE, Blocks.WARPED_FENCE);
        register(Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_FENCE); // Keep as is

        // === FENCE GATES ===
        register(Blocks.OAK_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        register(Blocks.SPRUCE_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        register(Blocks.BIRCH_FENCE_GATE, Blocks.WARPED_FENCE_GATE);
        register(Blocks.JUNGLE_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        register(Blocks.ACACIA_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        register(Blocks.DARK_OAK_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        register(Blocks.MANGROVE_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        register(Blocks.CHERRY_FENCE_GATE, Blocks.WARPED_FENCE_GATE);
        register(Blocks.BAMBOO_FENCE_GATE, Blocks.WARPED_FENCE_GATE);

        // === DOORS & TRAPDOORS ===
        register(Blocks.OAK_DOOR, Blocks.CRIMSON_DOOR);
        register(Blocks.SPRUCE_DOOR, Blocks.CRIMSON_DOOR);
        register(Blocks.BIRCH_DOOR, Blocks.WARPED_DOOR);
        register(Blocks.JUNGLE_DOOR, Blocks.CRIMSON_DOOR);
        register(Blocks.ACACIA_DOOR, Blocks.CRIMSON_DOOR);
        register(Blocks.DARK_OAK_DOOR, Blocks.CRIMSON_DOOR);
        register(Blocks.MANGROVE_DOOR, Blocks.CRIMSON_DOOR);
        register(Blocks.CHERRY_DOOR, Blocks.WARPED_DOOR);
        register(Blocks.BAMBOO_DOOR, Blocks.WARPED_DOOR);
        register(Blocks.OAK_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        register(Blocks.SPRUCE_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        register(Blocks.BIRCH_TRAPDOOR, Blocks.WARPED_TRAPDOOR);
        register(Blocks.JUNGLE_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        register(Blocks.ACACIA_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        register(Blocks.DARK_OAK_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        register(Blocks.MANGROVE_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        register(Blocks.CHERRY_TRAPDOOR, Blocks.WARPED_TRAPDOOR);
        register(Blocks.BAMBOO_TRAPDOOR, Blocks.WARPED_TRAPDOOR);

        // === BUTTONS & PRESSURE PLATES ===
        register(Blocks.OAK_BUTTON, Blocks.CRIMSON_BUTTON);
        register(Blocks.SPRUCE_BUTTON, Blocks.CRIMSON_BUTTON);
        register(Blocks.BIRCH_BUTTON, Blocks.WARPED_BUTTON);
        register(Blocks.JUNGLE_BUTTON, Blocks.CRIMSON_BUTTON);
        register(Blocks.ACACIA_BUTTON, Blocks.CRIMSON_BUTTON);
        register(Blocks.DARK_OAK_BUTTON, Blocks.CRIMSON_BUTTON);
        register(Blocks.MANGROVE_BUTTON, Blocks.CRIMSON_BUTTON);
        register(Blocks.CHERRY_BUTTON, Blocks.WARPED_BUTTON);
        register(Blocks.BAMBOO_BUTTON, Blocks.WARPED_BUTTON);
        register(Blocks.STONE_BUTTON, Blocks.POLISHED_BLACKSTONE_BUTTON);
        register(Blocks.OAK_PRESSURE_PLATE, Blocks.CRIMSON_PRESSURE_PLATE);
        register(Blocks.SPRUCE_PRESSURE_PLATE, Blocks.CRIMSON_PRESSURE_PLATE);
        register(Blocks.BIRCH_PRESSURE_PLATE, Blocks.WARPED_PRESSURE_PLATE);
        register(Blocks.JUNGLE_PRESSURE_PLATE, Blocks.CRIMSON_PRESSURE_PLATE);
        register(Blocks.ACACIA_PRESSURE_PLATE, Blocks.CRIMSON_PRESSURE_PLATE);
        register(Blocks.DARK_OAK_PRESSURE_PLATE, Blocks.CRIMSON_PRESSURE_PLATE);
        register(Blocks.MANGROVE_PRESSURE_PLATE, Blocks.CRIMSON_PRESSURE_PLATE);
        register(Blocks.CHERRY_PRESSURE_PLATE, Blocks.WARPED_PRESSURE_PLATE);
        register(Blocks.BAMBOO_PRESSURE_PLATE, Blocks.WARPED_PRESSURE_PLATE);
        register(Blocks.STONE_PRESSURE_PLATE, Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE);

        // === SIGNS ===
        register(Blocks.OAK_SIGN, Blocks.CRIMSON_SIGN);
        register(Blocks.OAK_WALL_SIGN, Blocks.CRIMSON_WALL_SIGN);
        register(Blocks.SPRUCE_SIGN, Blocks.CRIMSON_SIGN);
        register(Blocks.SPRUCE_WALL_SIGN, Blocks.CRIMSON_WALL_SIGN);
        register(Blocks.BIRCH_SIGN, Blocks.WARPED_SIGN);
        register(Blocks.BIRCH_WALL_SIGN, Blocks.WARPED_WALL_SIGN);
        register(Blocks.JUNGLE_SIGN, Blocks.CRIMSON_SIGN);
        register(Blocks.JUNGLE_WALL_SIGN, Blocks.CRIMSON_WALL_SIGN);
        register(Blocks.ACACIA_SIGN, Blocks.CRIMSON_SIGN);
        register(Blocks.ACACIA_WALL_SIGN, Blocks.CRIMSON_WALL_SIGN);
        register(Blocks.DARK_OAK_SIGN, Blocks.CRIMSON_SIGN);
        register(Blocks.DARK_OAK_WALL_SIGN, Blocks.CRIMSON_WALL_SIGN);
    }

    /**
     * Register a simple block-to-block transformation.
     */
    private static void register(Block from, Block to) {
        SIMPLE_TRANSFORMATIONS.put(from, to);
    }

    /**
     * Register a log transformation that preserves the axis property.
     */
    private static void registerLogTransformation(Block from, Block to) {
        STATE_TRANSFORMATIONS.put(from, state -> {
            BlockState newState = to.getDefaultState();
            if (state.contains(Properties.AXIS) && newState.contains(Properties.AXIS)) {
                newState = newState.with(Properties.AXIS, state.get(Properties.AXIS));
            }
            return newState;
        });
    }

    /**
     * Register a slab transformation that preserves TYPE and WATERLOGGED properties.
     */
    private static void registerSlabTransformation(Block from, Block to) {
        STATE_TRANSFORMATIONS.put(from, state -> {
            BlockState newState = to.getDefaultState();
            // Preserve slab type (top, bottom, double)
            if (state.contains(Properties.SLAB_TYPE) && newState.contains(Properties.SLAB_TYPE)) {
                newState = newState.with(Properties.SLAB_TYPE, state.get(Properties.SLAB_TYPE));
            }
            // Preserve waterlogged state
            if (state.contains(Properties.WATERLOGGED) && newState.contains(Properties.WATERLOGGED)) {
                newState = newState.with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED));
            }
            return newState;
        });
    }

    /**
     * Register a stair transformation that preserves FACING, HALF, SHAPE, and WATERLOGGED properties.
     */
    private static void registerStairTransformation(Block from, Block to) {
        STATE_TRANSFORMATIONS.put(from, state -> {
            BlockState newState = to.getDefaultState();
            // Preserve facing direction
            if (state.contains(Properties.HORIZONTAL_FACING) && newState.contains(Properties.HORIZONTAL_FACING)) {
                newState = newState.with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            }
            // Preserve half (top/bottom)
            if (state.contains(Properties.BLOCK_HALF) && newState.contains(Properties.BLOCK_HALF)) {
                newState = newState.with(Properties.BLOCK_HALF, state.get(Properties.BLOCK_HALF));
            }
            // Preserve stair shape (straight, inner_left, inner_right, outer_left, outer_right)
            if (state.contains(Properties.STAIR_SHAPE) && newState.contains(Properties.STAIR_SHAPE)) {
                newState = newState.with(Properties.STAIR_SHAPE, state.get(Properties.STAIR_SHAPE));
            }
            // Preserve waterlogged state
            if (state.contains(Properties.WATERLOGGED) && newState.contains(Properties.WATERLOGGED)) {
                newState = newState.with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED));
            }
            return newState;
        });
    }

    // Base nether materials for random diversity
    private static final Block[] BASE_NETHER_TERRAIN = {
        Blocks.NETHERRACK,
        Blocks.SOUL_SOIL,
        Blocks.SOUL_SAND,
        Blocks.BASALT,
        Blocks.BLACKSTONE,
        Blocks.MAGMA_BLOCK
    };

    private static final Block[] BASE_NETHER_STONE = {
        Blocks.BASALT,
        Blocks.BLACKSTONE,
        Blocks.NETHERRACK,
        Blocks.POLISHED_BASALT,
        Blocks.POLISHED_BLACKSTONE
    };

    private static final java.util.Random RANDOM = new java.util.Random();

    /**
     * Get the transformation result for a block state.
     * Has a 20% chance to substitute a random alternative base nether material.
     *
     * @param input The input block state
     * @return The transformed block state, or null if no transformation exists
     */
    public static BlockState getTransformation(BlockState input) {
        Block inputBlock = input.getBlock();

        // Check state-preserving transformations first
        if (STATE_TRANSFORMATIONS.containsKey(inputBlock)) {
            return STATE_TRANSFORMATIONS.get(inputBlock).apply(input);
        }

        // Check simple block transformations
        if (SIMPLE_TRANSFORMATIONS.containsKey(inputBlock)) {
            Block outputBlock = SIMPLE_TRANSFORMATIONS.get(inputBlock);
            // Don't transform to the same block
            if (outputBlock == inputBlock) {
                return null;
            }

            // 20% chance to substitute with random alternative
            if (RANDOM.nextFloat() < 0.20f) {
                outputBlock = getRandomAlternative(outputBlock);
            }

            return outputBlock.getDefaultState();
        }

        // No transformation available
        return null;
    }

    /**
     * Get a random alternative block for diversity.
     * Returns the original if no alternatives apply.
     */
    private static Block getRandomAlternative(Block original) {
        // Terrain blocks (grass, dirt, etc -> netherrack/soul soil)
        if (original == Blocks.NETHERRACK || original == Blocks.SOUL_SOIL || original == Blocks.SOUL_SAND) {
            return BASE_NETHER_TERRAIN[RANDOM.nextInt(BASE_NETHER_TERRAIN.length)];
        }

        // Stone blocks (stone, cobble, etc -> basalt/blackstone)
        if (original == Blocks.BASALT || original == Blocks.BLACKSTONE) {
            return BASE_NETHER_STONE[RANDOM.nextInt(BASE_NETHER_STONE.length)];
        }

        // Wart blocks can swap between crimson/warped
        if (original == Blocks.NETHER_WART_BLOCK) {
            return RANDOM.nextBoolean() ? Blocks.NETHER_WART_BLOCK : Blocks.WARPED_WART_BLOCK;
        }
        if (original == Blocks.WARPED_WART_BLOCK) {
            return RANDOM.nextBoolean() ? Blocks.WARPED_WART_BLOCK : Blocks.NETHER_WART_BLOCK;
        }

        // Stems can swap between crimson/warped
        if (original == Blocks.CRIMSON_STEM) {
            return RANDOM.nextBoolean() ? Blocks.CRIMSON_STEM : Blocks.WARPED_STEM;
        }
        if (original == Blocks.WARPED_STEM) {
            return RANDOM.nextBoolean() ? Blocks.WARPED_STEM : Blocks.CRIMSON_STEM;
        }

        // Nether bricks can become red nether bricks
        if (original == Blocks.NETHER_BRICKS) {
            return RANDOM.nextBoolean() ? Blocks.NETHER_BRICKS : Blocks.RED_NETHER_BRICKS;
        }

        // Roots/sprouts can vary
        if (original == Blocks.CRIMSON_ROOTS || original == Blocks.WARPED_ROOTS || original == Blocks.NETHER_SPROUTS) {
            Block[] roots = {Blocks.CRIMSON_ROOTS, Blocks.WARPED_ROOTS, Blocks.NETHER_SPROUTS};
            return roots[RANDOM.nextInt(roots.length)];
        }

        // Fungus can vary
        if (original == Blocks.CRIMSON_FUNGUS || original == Blocks.WARPED_FUNGUS) {
            return RANDOM.nextBoolean() ? Blocks.CRIMSON_FUNGUS : Blocks.WARPED_FUNGUS;
        }

        return original;
    }

    /**
     * Check if a block can be transformed.
     */
    public static boolean canTransform(BlockState state) {
        Block block = state.getBlock();
        return SIMPLE_TRANSFORMATIONS.containsKey(block) || STATE_TRANSFORMATIONS.containsKey(block);
    }

    /**
     * Check if a block is immune to corruption.
     */
    public static boolean isImmune(BlockState state) {
        Block block = state.getBlock();

        // Portal frame blocks are immune
        if (block == Blocks.OBSIDIAN || block == Blocks.CRYING_OBSIDIAN) {
            return true;
        }

        // Unbreakable blocks
        if (block == Blocks.BEDROCK || block == Blocks.BARRIER ||
                block == Blocks.END_PORTAL_FRAME || block == Blocks.END_PORTAL ||
                block == Blocks.COMMAND_BLOCK || block == Blocks.CHAIN_COMMAND_BLOCK ||
                block == Blocks.REPEATING_COMMAND_BLOCK || block == Blocks.STRUCTURE_BLOCK ||
                block == Blocks.JIGSAW) {
            return true;
        }

        // Nether blocks are immune (already corrupted)
        if (isNetherBlock(block)) {
            return true;
        }

        // Air is immune
        if (state.isAir()) {
            return true;
        }

        return false;
    }

    // ==================== MATURATION SYSTEM ====================
    // Nether blocks can "mature" or "worsen" based on neighboring blocks
    // This creates diverse, organic corruption patterns

    /**
     * Attempt to mature/evolve a nether block based on its neighbors.
     * Returns a new state if the block should evolve, null otherwise.
     */
    public static BlockState getMatureTransformation(BlockState currentState, NeighborContext neighbors) {
        Block block = currentState.getBlock();

        // Netherrack can become nylium if near fungus/stems
        if (block == Blocks.NETHERRACK) {
            if (neighbors.hasCrimsonInfluence() && neighbors.random.nextFloat() < 0.3f) {
                return Blocks.CRIMSON_NYLIUM.getDefaultState();
            }
            if (neighbors.hasWarpedInfluence() && neighbors.random.nextFloat() < 0.3f) {
                return Blocks.WARPED_NYLIUM.getDefaultState();
            }
            // Near lava, can become magma
            if (neighbors.hasLava() && neighbors.random.nextFloat() < 0.15f) {
                return Blocks.MAGMA_BLOCK.getDefaultState();
            }
        }

        // Soul soil can become soul sand (sinking effect spreads)
        if (block == Blocks.SOUL_SOIL) {
            if (neighbors.hasSoulSand() && neighbors.random.nextFloat() < 0.25f) {
                return Blocks.SOUL_SAND.getDefaultState();
            }
        }

        // Basalt can become polished or smooth variants
        if (block == Blocks.BASALT) {
            if (neighbors.hasPolishedStone() && neighbors.random.nextFloat() < 0.2f) {
                return Blocks.POLISHED_BASALT.getDefaultState();
            }
            if (neighbors.hasBlackstone() && neighbors.random.nextFloat() < 0.15f) {
                return Blocks.BLACKSTONE.getDefaultState();
            }
        }

        // Blackstone can become gilded near gold
        if (block == Blocks.BLACKSTONE) {
            if (neighbors.hasGold() && neighbors.random.nextFloat() < 0.1f) {
                return Blocks.GILDED_BLACKSTONE.getDefaultState();
            }
            if (neighbors.hasPolishedStone() && neighbors.random.nextFloat() < 0.2f) {
                return Blocks.POLISHED_BLACKSTONE.getDefaultState();
            }
        }

        // Nether wart blocks can spawn shroomlight (rare)
        if (block == Blocks.NETHER_WART_BLOCK || block == Blocks.WARPED_WART_BLOCK) {
            if (neighbors.random.nextFloat() < 0.05f) {
                return Blocks.SHROOMLIGHT.getDefaultState();
            }
        }

        // Crimson/Warped nylium can spread roots
        if (block == Blocks.CRIMSON_NYLIUM) {
            if (neighbors.hasAirAbove() && neighbors.random.nextFloat() < 0.2f) {
                // This signals to place roots above, handled separately
                return null;
            }
        }

        // Magma blocks near water create blackstone crust
        if (block == Blocks.MAGMA_BLOCK) {
            if (neighbors.hasWater() && neighbors.random.nextFloat() < 0.3f) {
                return Blocks.BLACKSTONE.getDefaultState();
            }
            // Note: Lava expansion logic for magma is handled below in LAVA EXPANSION section
        }

        // Lava source blocks can rarely solidify edges, but can also EXPAND
        if (block == Blocks.LAVA) {
            // Lava can cool at edges
            if (neighbors.hasCoolingSurface() && neighbors.random.nextFloat() < 0.05f) {
                return Blocks.MAGMA_BLOCK.getDefaultState();
            }
        }

        // === LAVA EXPANSION ===
        // Magma blocks adjacent to multiple lava sources melt faster (helps puddles connect)
        if (block == Blocks.MAGMA_BLOCK) {
            int lavaCount = neighbors.getLavaCount();
            // More lava neighbors = higher chance to melt
            if (lavaCount >= 2 && neighbors.random.nextFloat() < 0.25f) {
                return Blocks.LAVA.getDefaultState();
            }
            if (lavaCount == 1 && neighbors.random.nextFloat() < 0.12f) {
                return Blocks.LAVA.getDefaultState();
            }
        }

        // Netherrack near lots of lava becomes magma (prepares for lava expansion)
        if (block == Blocks.NETHERRACK) {
            if (neighbors.getLavaCount() >= 2 && neighbors.random.nextFloat() < 0.2f) {
                return Blocks.MAGMA_BLOCK.getDefaultState();
            }
        }

        // Blackstone near lava can become magma
        if (block == Blocks.BLACKSTONE || block == Blocks.POLISHED_BLACKSTONE) {
            if (neighbors.getLavaCount() >= 1 && neighbors.random.nextFloat() < 0.1f) {
                return Blocks.MAGMA_BLOCK.getDefaultState();
            }
        }

        return null;
    }

    /**
     * Context about neighboring blocks for maturation decisions.
     */
    public static class NeighborContext {
        public final net.minecraft.util.math.random.Random random;
        private boolean crimsonInfluence;
        private boolean warpedInfluence;
        private boolean lava;
        private int lavaCount; // Count of adjacent lava blocks for expansion logic
        private boolean soulSand;
        private boolean polishedStone;
        private boolean blackstone;
        private boolean gold;
        private boolean airAbove;
        private boolean water;
        private boolean coolingSurface;
        private boolean magma;
        private int magmaCount;

        public NeighborContext(net.minecraft.util.math.random.Random random) {
            this.random = random;
        }

        public void analyze(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
            for (net.minecraft.util.math.Direction dir : net.minecraft.util.math.Direction.values()) {
                BlockState neighbor = world.getBlockState(pos.offset(dir));
                Block block = neighbor.getBlock();

                if (block == Blocks.CRIMSON_STEM || block == Blocks.CRIMSON_FUNGUS ||
                    block == Blocks.CRIMSON_NYLIUM || block == Blocks.CRIMSON_ROOTS ||
                    block == Blocks.NETHER_WART_BLOCK) {
                    crimsonInfluence = true;
                }
                if (block == Blocks.WARPED_STEM || block == Blocks.WARPED_FUNGUS ||
                    block == Blocks.WARPED_NYLIUM || block == Blocks.WARPED_ROOTS ||
                    block == Blocks.WARPED_WART_BLOCK) {
                    warpedInfluence = true;
                }
                if (block == Blocks.LAVA) {
                    lava = true;
                    lavaCount++;
                }
                if (block == Blocks.MAGMA_BLOCK) {
                    magma = true;
                    magmaCount++;
                }
                if (block == Blocks.SOUL_SAND) soulSand = true;
                if (block == Blocks.POLISHED_BASALT || block == Blocks.POLISHED_BLACKSTONE ||
                    block == Blocks.POLISHED_BLACKSTONE_BRICKS) {
                    polishedStone = true;
                }
                if (block == Blocks.BLACKSTONE || block == Blocks.POLISHED_BLACKSTONE) {
                    blackstone = true;
                }
                if (block == Blocks.GOLD_BLOCK || block == Blocks.NETHER_GOLD_ORE ||
                    block == Blocks.GILDED_BLACKSTONE) {
                    gold = true;
                }
                if (neighbor.isAir() && dir == net.minecraft.util.math.Direction.UP) {
                    airAbove = true;
                }
                if (block == Blocks.WATER) water = true;
                if (neighbor.isAir() || block == Blocks.WATER) {
                    coolingSurface = true;
                }
            }
        }

        public boolean hasCrimsonInfluence() { return crimsonInfluence; }
        public boolean hasWarpedInfluence() { return warpedInfluence; }
        public boolean hasLava() { return lava; }
        public int getLavaCount() { return lavaCount; }
        public boolean hasMagma() { return magma; }
        public int getMagmaCount() { return magmaCount; }
        public boolean hasSoulSand() { return soulSand; }
        public boolean hasPolishedStone() { return polishedStone; }
        public boolean hasBlackstone() { return blackstone; }
        public boolean hasGold() { return gold; }
        public boolean hasAirAbove() { return airAbove; }
        public boolean hasWater() { return water; }
        public boolean hasCoolingSurface() { return coolingSurface; }
    }

    /**
     * Check if a block is a nether block (already corrupted).
     */
    private static boolean isNetherBlock(Block block) {
        return block == Blocks.NETHERRACK ||
                block == Blocks.SOUL_SAND ||
                block == Blocks.SOUL_SOIL ||
                block == Blocks.BASALT ||
                block == Blocks.POLISHED_BASALT ||
                block == Blocks.SMOOTH_BASALT ||
                block == Blocks.BLACKSTONE ||
                block == Blocks.POLISHED_BLACKSTONE ||
                block == Blocks.POLISHED_BLACKSTONE_BRICKS ||
                block == Blocks.CHISELED_POLISHED_BLACKSTONE ||
                block == Blocks.GILDED_BLACKSTONE ||
                block == Blocks.NETHER_BRICKS ||
                block == Blocks.RED_NETHER_BRICKS ||
                block == Blocks.CRACKED_NETHER_BRICKS ||
                block == Blocks.CHISELED_NETHER_BRICKS ||
                block == Blocks.NETHER_WART_BLOCK ||
                block == Blocks.WARPED_WART_BLOCK ||
                block == Blocks.CRIMSON_STEM ||
                block == Blocks.WARPED_STEM ||
                block == Blocks.CRIMSON_HYPHAE ||
                block == Blocks.WARPED_HYPHAE ||
                block == Blocks.CRIMSON_PLANKS ||
                block == Blocks.WARPED_PLANKS ||
                block == Blocks.CRIMSON_NYLIUM ||
                block == Blocks.WARPED_NYLIUM ||
                block == Blocks.CRIMSON_FUNGUS ||
                block == Blocks.WARPED_FUNGUS ||
                block == Blocks.CRIMSON_ROOTS ||
                block == Blocks.WARPED_ROOTS ||
                block == Blocks.NETHER_SPROUTS ||
                block == Blocks.WEEPING_VINES ||
                block == Blocks.WEEPING_VINES_PLANT ||
                block == Blocks.TWISTING_VINES ||
                block == Blocks.TWISTING_VINES_PLANT ||
                block == Blocks.SHROOMLIGHT ||
                block == Blocks.GLOWSTONE ||
                block == Blocks.MAGMA_BLOCK ||
                block == Blocks.NETHER_GOLD_ORE ||
                block == Blocks.NETHER_QUARTZ_ORE ||
                block == Blocks.ANCIENT_DEBRIS ||
                block == Blocks.LAVA ||
                block == Blocks.NETHER_PORTAL ||
                block == Blocks.SOUL_TORCH ||
                block == Blocks.SOUL_WALL_TORCH ||
                block == Blocks.SOUL_LANTERN ||
                block == Blocks.SOUL_CAMPFIRE ||
                block == Blocks.SOUL_FIRE;
    }
}
