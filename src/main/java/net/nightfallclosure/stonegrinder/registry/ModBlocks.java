package net.nightfallclosure.stonegrinder.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;
import net.nightfallclosure.stonegrinder.block.custom.GrinderBlock;

public class ModBlocks {
    public static Item GRINDER_BLOCK_ITEM;
    public static Block GRINDER_BLOCK;

    private static Pair<Item, Block> registerBlock(String name, Block block, ItemGroup tab) {
        Item registeredItem = registerBlockItem(name, block, tab);
        Block registeredBlock = Registry.register(Registry.BLOCK,
                new Identifier(StoneGrinderMod.MOD_ID, name), block);
        return new Pair<>(registeredItem, registeredBlock);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(StoneGrinderMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
        StoneGrinderMod.LOGGER.debug("Registering mod blocks for " + StoneGrinderMod.MOD_ID);
        Block unregisteredGrinderBlock = new GrinderBlock(FabricBlockSettings
                .of(Material.STONE)
                .strength(3.5f, 17.5f)
                .requiresTool()
                .sounds(BlockSoundGroup.STONE));

        Pair<Item, Block> registeredItemBlockPair =
                registerBlock("grinder", unregisteredGrinderBlock, ItemGroup.DECORATIONS);
        GRINDER_BLOCK_ITEM = registeredItemBlockPair.getLeft();
        GRINDER_BLOCK = registeredItemBlockPair.getRight();
    }
}
