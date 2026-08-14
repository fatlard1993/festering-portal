package com.festeringportal.corruption;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

        // === STONE BRICKS ===
        register(Blocks.STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
        register(Blocks.MOSSY_STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
        register(Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_NETHER_BRICKS);
        register(Blocks.CHISELED_STONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE);

        // === BRICKS ===
        register(Blocks.BRICKS, Blocks.NETHER_BRICKS);

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

        // Stripped wood
        registerLogTransformation(Blocks.STRIPPED_OAK_WOOD, Blocks.STRIPPED_CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.STRIPPED_SPRUCE_WOOD, Blocks.STRIPPED_CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.STRIPPED_BIRCH_WOOD, Blocks.STRIPPED_WARPED_HYPHAE);
        registerLogTransformation(Blocks.STRIPPED_JUNGLE_WOOD, Blocks.STRIPPED_CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.STRIPPED_ACACIA_WOOD, Blocks.STRIPPED_CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.STRIPPED_CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.STRIPPED_MANGROVE_WOOD, Blocks.STRIPPED_CRIMSON_HYPHAE);
        registerLogTransformation(Blocks.STRIPPED_CHERRY_WOOD, Blocks.STRIPPED_WARPED_HYPHAE);

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

        // === FENCE GATES (preserve facing/open/in_wall/powered) ===
        registerFenceGateTransformation(Blocks.OAK_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        registerFenceGateTransformation(Blocks.SPRUCE_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        registerFenceGateTransformation(Blocks.BIRCH_FENCE_GATE, Blocks.WARPED_FENCE_GATE);
        registerFenceGateTransformation(Blocks.JUNGLE_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        registerFenceGateTransformation(Blocks.ACACIA_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        registerFenceGateTransformation(Blocks.DARK_OAK_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        registerFenceGateTransformation(Blocks.MANGROVE_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE);
        registerFenceGateTransformation(Blocks.CHERRY_FENCE_GATE, Blocks.WARPED_FENCE_GATE);
        registerFenceGateTransformation(Blocks.BAMBOO_FENCE_GATE, Blocks.WARPED_FENCE_GATE);

        // === DOORS (preserve facing/half/hinge/open/powered) ===
        registerDoorTransformation(Blocks.OAK_DOOR, Blocks.CRIMSON_DOOR);
        registerDoorTransformation(Blocks.SPRUCE_DOOR, Blocks.CRIMSON_DOOR);
        registerDoorTransformation(Blocks.BIRCH_DOOR, Blocks.WARPED_DOOR);
        registerDoorTransformation(Blocks.JUNGLE_DOOR, Blocks.CRIMSON_DOOR);
        registerDoorTransformation(Blocks.ACACIA_DOOR, Blocks.CRIMSON_DOOR);
        registerDoorTransformation(Blocks.DARK_OAK_DOOR, Blocks.CRIMSON_DOOR);
        registerDoorTransformation(Blocks.MANGROVE_DOOR, Blocks.CRIMSON_DOOR);
        registerDoorTransformation(Blocks.CHERRY_DOOR, Blocks.WARPED_DOOR);
        registerDoorTransformation(Blocks.BAMBOO_DOOR, Blocks.WARPED_DOOR);

        // === TRAPDOORS (preserve facing/half/open/powered/waterlogged) ===
        registerTrapdoorTransformation(Blocks.OAK_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        registerTrapdoorTransformation(Blocks.SPRUCE_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        registerTrapdoorTransformation(Blocks.BIRCH_TRAPDOOR, Blocks.WARPED_TRAPDOOR);
        registerTrapdoorTransformation(Blocks.JUNGLE_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        registerTrapdoorTransformation(Blocks.ACACIA_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        registerTrapdoorTransformation(Blocks.DARK_OAK_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        registerTrapdoorTransformation(Blocks.MANGROVE_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR);
        registerTrapdoorTransformation(Blocks.CHERRY_TRAPDOOR, Blocks.WARPED_TRAPDOOR);
        registerTrapdoorTransformation(Blocks.BAMBOO_TRAPDOOR, Blocks.WARPED_TRAPDOOR);

        // === BUTTONS (preserve face/facing/powered) ===
        registerButtonTransformation(Blocks.OAK_BUTTON, Blocks.CRIMSON_BUTTON);
        registerButtonTransformation(Blocks.SPRUCE_BUTTON, Blocks.CRIMSON_BUTTON);
        registerButtonTransformation(Blocks.BIRCH_BUTTON, Blocks.WARPED_BUTTON);
        registerButtonTransformation(Blocks.JUNGLE_BUTTON, Blocks.CRIMSON_BUTTON);
        registerButtonTransformation(Blocks.ACACIA_BUTTON, Blocks.CRIMSON_BUTTON);
        registerButtonTransformation(Blocks.DARK_OAK_BUTTON, Blocks.CRIMSON_BUTTON);
        registerButtonTransformation(Blocks.MANGROVE_BUTTON, Blocks.CRIMSON_BUTTON);
        registerButtonTransformation(Blocks.CHERRY_BUTTON, Blocks.WARPED_BUTTON);
        registerButtonTransformation(Blocks.BAMBOO_BUTTON, Blocks.WARPED_BUTTON);
        registerButtonTransformation(Blocks.STONE_BUTTON, Blocks.POLISHED_BLACKSTONE_BUTTON);

        // === PRESSURE PLATES ===
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

        // === WOOL (warm colors -> NETHER_WART_BLOCK, cool colors -> WARPED_WART_BLOCK) ===
        register(Blocks.WOOL.white(), Blocks.NETHER_WART_BLOCK);
        register(Blocks.WOOL.orange(), Blocks.NETHER_WART_BLOCK);
        register(Blocks.WOOL.magenta(), Blocks.WARPED_WART_BLOCK);
        register(Blocks.WOOL.lightBlue(), Blocks.WARPED_WART_BLOCK);
        register(Blocks.WOOL.yellow(), Blocks.NETHER_WART_BLOCK);
        register(Blocks.WOOL.lime(), Blocks.WARPED_WART_BLOCK);
        register(Blocks.WOOL.pink(), Blocks.NETHER_WART_BLOCK);
        register(Blocks.WOOL.gray(), Blocks.NETHER_WART_BLOCK);
        register(Blocks.WOOL.lightGray(), Blocks.WARPED_WART_BLOCK);
        register(Blocks.WOOL.cyan(), Blocks.WARPED_WART_BLOCK);
        register(Blocks.WOOL.purple(), Blocks.WARPED_WART_BLOCK);
        register(Blocks.WOOL.blue(), Blocks.WARPED_WART_BLOCK);
        register(Blocks.WOOL.brown(), Blocks.NETHER_WART_BLOCK);
        register(Blocks.WOOL.green(), Blocks.WARPED_WART_BLOCK);
        register(Blocks.WOOL.red(), Blocks.NETHER_WART_BLOCK);
        register(Blocks.WOOL.black(), Blocks.NETHER_WART_BLOCK);

        // === CARPET (warm -> NETHER_SPROUTS, cool -> WARPED_ROOTS) ===
        register(Blocks.CARPET.white(), Blocks.NETHER_SPROUTS);
        register(Blocks.CARPET.orange(), Blocks.NETHER_SPROUTS);
        register(Blocks.CARPET.magenta(), Blocks.WARPED_ROOTS);
        register(Blocks.CARPET.lightBlue(), Blocks.WARPED_ROOTS);
        register(Blocks.CARPET.yellow(), Blocks.NETHER_SPROUTS);
        register(Blocks.CARPET.lime(), Blocks.WARPED_ROOTS);
        register(Blocks.CARPET.pink(), Blocks.NETHER_SPROUTS);
        register(Blocks.CARPET.gray(), Blocks.NETHER_SPROUTS);
        register(Blocks.CARPET.lightGray(), Blocks.WARPED_ROOTS);
        register(Blocks.CARPET.cyan(), Blocks.WARPED_ROOTS);
        register(Blocks.CARPET.purple(), Blocks.WARPED_ROOTS);
        register(Blocks.CARPET.blue(), Blocks.WARPED_ROOTS);
        register(Blocks.CARPET.brown(), Blocks.NETHER_SPROUTS);
        register(Blocks.CARPET.green(), Blocks.WARPED_ROOTS);
        register(Blocks.CARPET.red(), Blocks.NETHER_SPROUTS);
        register(Blocks.CARPET.black(), Blocks.NETHER_SPROUTS);

        // === CONCRETE (all colors -> BLACKSTONE) ===
        register(Blocks.CONCRETE.white(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.orange(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.magenta(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.lightBlue(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.yellow(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.lime(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.pink(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.gray(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.lightGray(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.cyan(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.purple(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.blue(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.brown(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.green(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.red(), Blocks.BLACKSTONE);
        register(Blocks.CONCRETE.black(), Blocks.BLACKSTONE);

        // === CONCRETE POWDER (all colors -> SOUL_SAND) ===
        register(Blocks.CONCRETE_POWDER.white(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.orange(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.magenta(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.lightBlue(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.yellow(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.lime(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.pink(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.gray(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.lightGray(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.cyan(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.purple(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.blue(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.brown(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.green(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.red(), Blocks.SOUL_SAND);
        register(Blocks.CONCRETE_POWDER.black(), Blocks.SOUL_SAND);

        // === TERRACOTTA (unglazed -> NETHERRACK, glazed -> NETHER_BRICKS) ===
        register(Blocks.TERRACOTTA, Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.white(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.orange(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.magenta(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.lightBlue(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.yellow(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.lime(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.pink(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.gray(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.lightGray(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.cyan(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.purple(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.blue(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.brown(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.green(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.red(), Blocks.NETHERRACK);
        register(Blocks.DYED_TERRACOTTA.black(), Blocks.NETHERRACK);
        register(Blocks.GLAZED_TERRACOTTA.white(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.orange(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.magenta(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.lightBlue(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.yellow(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.lime(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.pink(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.gray(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.lightGray(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.cyan(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.purple(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.blue(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.brown(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.green(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.red(), Blocks.NETHER_BRICKS);
        register(Blocks.GLAZED_TERRACOTTA.black(), Blocks.NETHER_BRICKS);

        // === STAINED GLASS (all colors -> TINTED_GLASS) ===
        register(Blocks.STAINED_GLASS.white(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.orange(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.magenta(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.lightBlue(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.yellow(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.lime(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.pink(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.gray(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.lightGray(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.cyan(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.purple(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.blue(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.brown(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.green(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.red(), Blocks.TINTED_GLASS);
        register(Blocks.STAINED_GLASS.black(), Blocks.TINTED_GLASS);

        // === STAINED GLASS PANES (all colors -> IRON_BARS) ===
        register(Blocks.STAINED_GLASS_PANE.white(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.orange(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.magenta(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.lightBlue(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.yellow(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.lime(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.pink(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.gray(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.lightGray(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.cyan(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.purple(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.blue(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.brown(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.green(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.red(), Blocks.IRON_BARS);
        register(Blocks.STAINED_GLASS_PANE.black(), Blocks.IRON_BARS);

        // === COPPER FAMILY ===
        register(Blocks.COPPER_BLOCK.weathering().unaffected(), Blocks.NETHER_BRICKS);
        register(Blocks.COPPER_BLOCK.weathering().exposed(), Blocks.NETHER_BRICKS);
        register(Blocks.COPPER_BLOCK.weathering().weathered(), Blocks.NETHER_BRICKS);
        register(Blocks.COPPER_BLOCK.weathering().oxidized(), Blocks.NETHER_BRICKS);
        register(Blocks.CUT_COPPER.weathering().unaffected(), Blocks.NETHER_BRICKS);
        register(Blocks.CUT_COPPER.weathering().exposed(), Blocks.NETHER_BRICKS);
        register(Blocks.CUT_COPPER.weathering().weathered(), Blocks.NETHER_BRICKS);
        register(Blocks.CUT_COPPER.weathering().oxidized(), Blocks.NETHER_BRICKS);
        register(Blocks.COPPER_BLOCK.waxed().unaffected(), Blocks.NETHER_BRICKS);
        register(Blocks.COPPER_BLOCK.waxed().exposed(), Blocks.NETHER_BRICKS);
        register(Blocks.COPPER_BLOCK.waxed().weathered(), Blocks.NETHER_BRICKS);
        register(Blocks.COPPER_BLOCK.waxed().oxidized(), Blocks.NETHER_BRICKS);
        register(Blocks.CUT_COPPER.waxed().unaffected(), Blocks.NETHER_BRICKS);
        register(Blocks.CUT_COPPER.waxed().exposed(), Blocks.NETHER_BRICKS);
        register(Blocks.CUT_COPPER.waxed().weathered(), Blocks.NETHER_BRICKS);
        register(Blocks.CUT_COPPER.waxed().oxidized(), Blocks.NETHER_BRICKS);

        // === PRISMARINE ===
        register(Blocks.PRISMARINE, Blocks.NETHER_BRICKS);
        register(Blocks.PRISMARINE_BRICKS, Blocks.NETHER_BRICKS);
        register(Blocks.DARK_PRISMARINE, Blocks.RED_NETHER_BRICKS);

        // === END STONE ===
        register(Blocks.END_STONE, Blocks.BASALT);
        register(Blocks.END_STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);

        // === PURPUR ===
        register(Blocks.PURPUR_BLOCK, Blocks.NETHER_BRICKS);
        register(Blocks.PURPUR_PILLAR, Blocks.BASALT);

        // === QUARTZ (overworld crafted blocks) ===
        register(Blocks.QUARTZ_BLOCK, Blocks.BASALT);
        register(Blocks.CHISELED_QUARTZ_BLOCK, Blocks.POLISHED_BASALT);
        register(Blocks.QUARTZ_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
        register(Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_BASALT);
        register(Blocks.QUARTZ_PILLAR, Blocks.POLISHED_BASALT);

        // === MUD BRICKS ===
        register(Blocks.MUD_BRICKS, Blocks.NETHER_BRICKS);

        // === MISC BUILDING BLOCKS ===
        register(Blocks.BOOKSHELF, Blocks.NETHER_WART_BLOCK);
        register(Blocks.CHISELED_BOOKSHELF, Blocks.NETHER_WART_BLOCK);
        register(Blocks.LADDER, Blocks.AIR);
        register(Blocks.SCAFFOLDING, Blocks.AIR);
        register(Blocks.POINTED_DRIPSTONE, Blocks.AIR);
        register(Blocks.HANGING_ROOTS, Blocks.WARPED_ROOTS);
        register(Blocks.SPORE_BLOSSOM, Blocks.CRIMSON_FUNGUS);
        register(Blocks.BIG_DRIPLEAF, Blocks.AIR);
        register(Blocks.BIG_DRIPLEAF_STEM, Blocks.AIR);
        register(Blocks.SMALL_DRIPLEAF, Blocks.AIR);

        // === AMETHYST ===
        register(Blocks.AMETHYST_BLOCK, Blocks.BASALT);
        register(Blocks.BUDDING_AMETHYST, Blocks.BASALT);
        register(Blocks.SMALL_AMETHYST_BUD, Blocks.AIR);
        register(Blocks.MEDIUM_AMETHYST_BUD, Blocks.AIR);
        register(Blocks.LARGE_AMETHYST_BUD, Blocks.AIR);
        register(Blocks.AMETHYST_CLUSTER, Blocks.AIR);
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
            BlockState newState = to.defaultBlockState();
            if (state.hasProperty(BlockStateProperties.AXIS) && newState.hasProperty(BlockStateProperties.AXIS)) {
                newState = newState.setValue(BlockStateProperties.AXIS, state.getValue(BlockStateProperties.AXIS));
            }
            return newState;
        });
    }

    /**
     * Register a slab transformation that preserves TYPE and WATERLOGGED properties.
     */
    private static void registerSlabTransformation(Block from, Block to) {
        STATE_TRANSFORMATIONS.put(from, state -> {
            BlockState newState = to.defaultBlockState();
            // Preserve slab type (top, bottom, double)
            if (state.hasProperty(BlockStateProperties.SLAB_TYPE) && newState.hasProperty(BlockStateProperties.SLAB_TYPE)) {
                newState = newState.setValue(BlockStateProperties.SLAB_TYPE, state.getValue(BlockStateProperties.SLAB_TYPE));
            }
            // Preserve waterlogged state
            if (state.hasProperty(BlockStateProperties.WATERLOGGED) && newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                newState = newState.setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
            }
            return newState;
        });
    }

    /**
     * Register a stair transformation that preserves FACING, HALF, SHAPE, and WATERLOGGED properties.
     */
    private static void registerStairTransformation(Block from, Block to) {
        STATE_TRANSFORMATIONS.put(from, state -> {
            BlockState newState = to.defaultBlockState();
            // Preserve facing direction
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
            }
            // Preserve half (top/bottom)
            if (state.hasProperty(BlockStateProperties.HALF) && newState.hasProperty(BlockStateProperties.HALF)) {
                newState = newState.setValue(BlockStateProperties.HALF, state.getValue(BlockStateProperties.HALF));
            }
            // Preserve stair shape (straight, inner_left, inner_right, outer_left, outer_right)
            if (state.hasProperty(BlockStateProperties.STAIRS_SHAPE) && newState.hasProperty(BlockStateProperties.STAIRS_SHAPE)) {
                newState = newState.setValue(BlockStateProperties.STAIRS_SHAPE, state.getValue(BlockStateProperties.STAIRS_SHAPE));
            }
            // Preserve waterlogged state
            if (state.hasProperty(BlockStateProperties.WATERLOGGED) && newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                newState = newState.setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
            }
            return newState;
        });
    }

    /**
     * Register a door transformation that preserves HORIZONTAL_FACING, DOUBLE_BLOCK_HALF,
     * DOOR_HINGE, OPEN, and POWERED properties.
     */
    private static void registerDoorTransformation(Block from, Block to) {
        STATE_TRANSFORMATIONS.put(from, state -> {
            BlockState newState = to.defaultBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
            }
            if (state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && newState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
                newState = newState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF));
            }
            if (state.hasProperty(BlockStateProperties.DOOR_HINGE) && newState.hasProperty(BlockStateProperties.DOOR_HINGE)) {
                newState = newState.setValue(BlockStateProperties.DOOR_HINGE, state.getValue(BlockStateProperties.DOOR_HINGE));
            }
            if (state.hasProperty(BlockStateProperties.OPEN) && newState.hasProperty(BlockStateProperties.OPEN)) {
                newState = newState.setValue(BlockStateProperties.OPEN, state.getValue(BlockStateProperties.OPEN));
            }
            if (state.hasProperty(BlockStateProperties.POWERED) && newState.hasProperty(BlockStateProperties.POWERED)) {
                newState = newState.setValue(BlockStateProperties.POWERED, state.getValue(BlockStateProperties.POWERED));
            }
            return newState;
        });
    }

    /**
     * Register a trapdoor transformation that preserves HORIZONTAL_FACING, BLOCK_HALF,
     * OPEN, POWERED, and WATERLOGGED properties.
     */
    private static void registerTrapdoorTransformation(Block from, Block to) {
        STATE_TRANSFORMATIONS.put(from, state -> {
            BlockState newState = to.defaultBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
            }
            if (state.hasProperty(BlockStateProperties.HALF) && newState.hasProperty(BlockStateProperties.HALF)) {
                newState = newState.setValue(BlockStateProperties.HALF, state.getValue(BlockStateProperties.HALF));
            }
            if (state.hasProperty(BlockStateProperties.OPEN) && newState.hasProperty(BlockStateProperties.OPEN)) {
                newState = newState.setValue(BlockStateProperties.OPEN, state.getValue(BlockStateProperties.OPEN));
            }
            if (state.hasProperty(BlockStateProperties.POWERED) && newState.hasProperty(BlockStateProperties.POWERED)) {
                newState = newState.setValue(BlockStateProperties.POWERED, state.getValue(BlockStateProperties.POWERED));
            }
            if (state.hasProperty(BlockStateProperties.WATERLOGGED) && newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                newState = newState.setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
            }
            return newState;
        });
    }

    /**
     * Register a fence gate transformation that preserves HORIZONTAL_FACING, IN_WALL,
     * OPEN, and POWERED properties.
     */
    private static void registerFenceGateTransformation(Block from, Block to) {
        STATE_TRANSFORMATIONS.put(from, state -> {
            BlockState newState = to.defaultBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
            }
            if (state.hasProperty(BlockStateProperties.IN_WALL) && newState.hasProperty(BlockStateProperties.IN_WALL)) {
                newState = newState.setValue(BlockStateProperties.IN_WALL, state.getValue(BlockStateProperties.IN_WALL));
            }
            if (state.hasProperty(BlockStateProperties.OPEN) && newState.hasProperty(BlockStateProperties.OPEN)) {
                newState = newState.setValue(BlockStateProperties.OPEN, state.getValue(BlockStateProperties.OPEN));
            }
            if (state.hasProperty(BlockStateProperties.POWERED) && newState.hasProperty(BlockStateProperties.POWERED)) {
                newState = newState.setValue(BlockStateProperties.POWERED, state.getValue(BlockStateProperties.POWERED));
            }
            return newState;
        });
    }

    /**
     * Register a button transformation that preserves BLOCK_FACE (floor/wall/ceiling),
     * HORIZONTAL_FACING, and POWERED properties.
     */
    private static void registerButtonTransformation(Block from, Block to) {
        STATE_TRANSFORMATIONS.put(from, state -> {
            BlockState newState = to.defaultBlockState();
            if (state.hasProperty(BlockStateProperties.ATTACH_FACE) && newState.hasProperty(BlockStateProperties.ATTACH_FACE)) {
                newState = newState.setValue(BlockStateProperties.ATTACH_FACE, state.getValue(BlockStateProperties.ATTACH_FACE));
            }
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
            }
            if (state.hasProperty(BlockStateProperties.POWERED) && newState.hasProperty(BlockStateProperties.POWERED)) {
                newState = newState.setValue(BlockStateProperties.POWERED, state.getValue(BlockStateProperties.POWERED));
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

    /**
     * Get the transformation result for a block state.
     * Has a 20% chance to substitute a random alternative base nether material.
     *
     * @param input The input block state
     * @param random The random source to use for diversity rolls
     * @return The transformed block state, or null if no transformation exists
     */
    public static BlockState getTransformation(BlockState input, net.minecraft.util.RandomSource random) {
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
            if (random.nextFloat() < 0.20f) {
                outputBlock = getRandomAlternative(outputBlock, random);
            }

            return outputBlock.defaultBlockState();
        }

        // No transformation available
        return null;
    }

    /**
     * Get a random alternative block for diversity.
     * Returns the original if no alternatives apply.
     */
    private static Block getRandomAlternative(Block original, net.minecraft.util.RandomSource random) {
        // Terrain blocks (grass, dirt, etc -> netherrack/soul soil)
        if (original == Blocks.NETHERRACK || original == Blocks.SOUL_SOIL || original == Blocks.SOUL_SAND) {
            return BASE_NETHER_TERRAIN[random.nextInt(BASE_NETHER_TERRAIN.length)];
        }

        // Stone blocks (stone, cobble, etc -> basalt/blackstone)
        if (original == Blocks.BASALT || original == Blocks.BLACKSTONE) {
            return BASE_NETHER_STONE[random.nextInt(BASE_NETHER_STONE.length)];
        }

        // Wart blocks can swap between crimson/warped
        if (original == Blocks.NETHER_WART_BLOCK) {
            return random.nextBoolean() ? Blocks.NETHER_WART_BLOCK : Blocks.WARPED_WART_BLOCK;
        }
        if (original == Blocks.WARPED_WART_BLOCK) {
            return random.nextBoolean() ? Blocks.WARPED_WART_BLOCK : Blocks.NETHER_WART_BLOCK;
        }

        // Stems can swap between crimson/warped
        if (original == Blocks.CRIMSON_STEM) {
            return random.nextBoolean() ? Blocks.CRIMSON_STEM : Blocks.WARPED_STEM;
        }
        if (original == Blocks.WARPED_STEM) {
            return random.nextBoolean() ? Blocks.WARPED_STEM : Blocks.CRIMSON_STEM;
        }

        // Nether bricks can become red nether bricks
        if (original == Blocks.NETHER_BRICKS) {
            return random.nextBoolean() ? Blocks.NETHER_BRICKS : Blocks.RED_NETHER_BRICKS;
        }

        // Roots/sprouts can vary
        if (original == Blocks.CRIMSON_ROOTS || original == Blocks.WARPED_ROOTS || original == Blocks.NETHER_SPROUTS) {
            Block[] roots = {Blocks.CRIMSON_ROOTS, Blocks.WARPED_ROOTS, Blocks.NETHER_SPROUTS};
            return roots[random.nextInt(roots.length)];
        }

        // Fungus can vary
        if (original == Blocks.CRIMSON_FUNGUS || original == Blocks.WARPED_FUNGUS) {
            return random.nextBoolean() ? Blocks.CRIMSON_FUNGUS : Blocks.WARPED_FUNGUS;
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
                block == Blocks.JIGSAW || block == Blocks.REINFORCED_DEEPSLATE) {
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

        // Netherrack can become nylium if near fungus/stems, or magma near lava
        if (block == Blocks.NETHERRACK) {
            // Graduated lava checks (higher lava count = higher chance)
            int lavaCount = neighbors.getLavaCount();
            if (lavaCount >= 2 && neighbors.random.nextFloat() < 0.25f) {
                return Blocks.MAGMA_BLOCK.defaultBlockState();
            }
            if (neighbors.hasLava() && neighbors.random.nextFloat() < 0.15f) {
                return Blocks.MAGMA_BLOCK.defaultBlockState();
            }
            // Nylium checks
            if (neighbors.hasCrimsonInfluence() && neighbors.random.nextFloat() < 0.3f) {
                return Blocks.CRIMSON_NYLIUM.defaultBlockState();
            }
            if (neighbors.hasWarpedInfluence() && neighbors.random.nextFloat() < 0.3f) {
                return Blocks.WARPED_NYLIUM.defaultBlockState();
            }
        }

        // Soul soil can become soul sand (sinking effect spreads)
        if (block == Blocks.SOUL_SOIL) {
            if (neighbors.hasSoulSand() && neighbors.random.nextFloat() < 0.25f) {
                return Blocks.SOUL_SAND.defaultBlockState();
            }
        }

        // Basalt can become polished or smooth variants
        if (block == Blocks.BASALT) {
            if (neighbors.hasPolishedStone() && neighbors.random.nextFloat() < 0.2f) {
                return Blocks.POLISHED_BASALT.defaultBlockState();
            }
            if (neighbors.hasBlackstone() && neighbors.random.nextFloat() < 0.15f) {
                return Blocks.BLACKSTONE.defaultBlockState();
            }
        }

        // Blackstone can become gilded near gold
        if (block == Blocks.BLACKSTONE) {
            if (neighbors.hasGold() && neighbors.random.nextFloat() < 0.1f) {
                return Blocks.GILDED_BLACKSTONE.defaultBlockState();
            }
            if (neighbors.hasPolishedStone() && neighbors.random.nextFloat() < 0.2f) {
                return Blocks.POLISHED_BLACKSTONE.defaultBlockState();
            }
        }

        // Nether wart blocks can spawn shroomlight (rare)
        if (block == Blocks.NETHER_WART_BLOCK || block == Blocks.WARPED_WART_BLOCK) {
            if (neighbors.random.nextFloat() < 0.05f) {
                return Blocks.SHROOMLIGHT.defaultBlockState();
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
                return Blocks.BLACKSTONE.defaultBlockState();
            }
        }

        // Lava source blocks can rarely solidify edges, but can also EXPAND
        if (block == Blocks.LAVA) {
            // Lava can cool at edges
            if (neighbors.hasCoolingSurface() && neighbors.random.nextFloat() < 0.05f) {
                return Blocks.MAGMA_BLOCK.defaultBlockState();
            }
        }

        // === LAVA EXPANSION ===
        // Magma blocks adjacent to multiple lava sources melt faster (helps puddles connect)
        if (block == Blocks.MAGMA_BLOCK) {
            int lavaCount = neighbors.getLavaCount();
            // More lava neighbors = higher chance to melt
            if (lavaCount >= 2 && neighbors.random.nextFloat() < 0.25f) {
                return Blocks.LAVA.defaultBlockState();
            }
            if (lavaCount == 1 && neighbors.random.nextFloat() < 0.12f) {
                return Blocks.LAVA.defaultBlockState();
            }
        }

        // Blackstone near lava can become magma
        if (block == Blocks.BLACKSTONE || block == Blocks.POLISHED_BLACKSTONE) {
            if (neighbors.getLavaCount() >= 1 && neighbors.random.nextFloat() < 0.1f) {
                return Blocks.MAGMA_BLOCK.defaultBlockState();
            }
        }

        return null;
    }

    /**
     * Context about neighboring blocks for maturation decisions.
     */
    public static class NeighborContext {
        public final net.minecraft.util.RandomSource random;
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

        public NeighborContext(net.minecraft.util.RandomSource random) {
            this.random = random;
        }

        public void analyze(net.minecraft.world.level.Level world, net.minecraft.core.BlockPos pos) {
            for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
                BlockState neighbor = world.getBlockState(pos.relative(dir));
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
                if (neighbor.isAir() && dir == net.minecraft.core.Direction.UP) {
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
    public static boolean isNetherBlock(Block block) {
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
                block == Blocks.STRIPPED_CRIMSON_STEM ||
                block == Blocks.STRIPPED_WARPED_STEM ||
                block == Blocks.CRIMSON_HYPHAE ||
                block == Blocks.WARPED_HYPHAE ||
                block == Blocks.STRIPPED_CRIMSON_HYPHAE ||
                block == Blocks.STRIPPED_WARPED_HYPHAE ||
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
                block == Blocks.BONE_BLOCK ||
                block == Blocks.LAVA ||
                block == Blocks.NETHER_PORTAL ||
                block == Blocks.SOUL_TORCH ||
                block == Blocks.SOUL_WALL_TORCH ||
                block == Blocks.SOUL_LANTERN ||
                block == Blocks.SOUL_CAMPFIRE ||
                block == Blocks.SOUL_FIRE ||
                // Slabs
                block == Blocks.CRIMSON_SLAB ||
                block == Blocks.WARPED_SLAB ||
                block == Blocks.BLACKSTONE_SLAB ||
                block == Blocks.POLISHED_BLACKSTONE_SLAB ||
                block == Blocks.POLISHED_BLACKSTONE_BRICK_SLAB ||
                block == Blocks.NETHER_BRICK_SLAB ||
                block == Blocks.RED_NETHER_BRICK_SLAB ||
                // Stairs
                block == Blocks.CRIMSON_STAIRS ||
                block == Blocks.WARPED_STAIRS ||
                block == Blocks.BLACKSTONE_STAIRS ||
                block == Blocks.POLISHED_BLACKSTONE_STAIRS ||
                block == Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS ||
                block == Blocks.NETHER_BRICK_STAIRS ||
                block == Blocks.RED_NETHER_BRICK_STAIRS ||
                // Walls
                block == Blocks.BLACKSTONE_WALL ||
                block == Blocks.POLISHED_BLACKSTONE_WALL ||
                block == Blocks.POLISHED_BLACKSTONE_BRICK_WALL ||
                block == Blocks.NETHER_BRICK_WALL ||
                block == Blocks.RED_NETHER_BRICK_WALL ||
                // Fences
                block == Blocks.CRIMSON_FENCE ||
                block == Blocks.WARPED_FENCE ||
                block == Blocks.NETHER_BRICK_FENCE ||
                // Fence gates
                block == Blocks.CRIMSON_FENCE_GATE ||
                block == Blocks.WARPED_FENCE_GATE ||
                // Doors
                block == Blocks.CRIMSON_DOOR ||
                block == Blocks.WARPED_DOOR ||
                // Trapdoors
                block == Blocks.CRIMSON_TRAPDOOR ||
                block == Blocks.WARPED_TRAPDOOR ||
                // Buttons
                block == Blocks.CRIMSON_BUTTON ||
                block == Blocks.WARPED_BUTTON ||
                block == Blocks.POLISHED_BLACKSTONE_BUTTON ||
                // Pressure plates
                block == Blocks.CRIMSON_PRESSURE_PLATE ||
                block == Blocks.WARPED_PRESSURE_PLATE ||
                block == Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE ||
                // Signs
                block == Blocks.CRIMSON_SIGN ||
                block == Blocks.CRIMSON_WALL_SIGN ||
                block == Blocks.WARPED_SIGN ||
                block == Blocks.WARPED_WALL_SIGN ||
                // Non-nether blocks that are transformation outputs
                block == Blocks.TINTED_GLASS ||
                block == Blocks.IRON_BARS;
    }
}
