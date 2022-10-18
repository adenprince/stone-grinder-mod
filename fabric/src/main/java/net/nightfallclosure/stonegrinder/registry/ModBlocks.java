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
import net.minecraft.util.registry.Registry;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;
import net.nightfallclosure.stonegrinder.block.custom.GrinderBlock;

public class ModBlocks {
    public static final Block GRINDER_BLOCK = new GrinderBlock(FabricBlockSettings
            .of(Material.STONE)
            .strength(3.5f, 17.5f)
            .requiresTool()
            .sounds(BlockSoundGroup.STONE));
    public static final Item GRINDER_BLOCK_ITEM = new BlockItem(GRINDER_BLOCK,
            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static void registerModBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(StoneGrinderMod.MOD_ID, "grinder"), GRINDER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(StoneGrinderMod.MOD_ID, "grinder"), GRINDER_BLOCK_ITEM);
    }
}
