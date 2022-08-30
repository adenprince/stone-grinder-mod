package net.nightfallclosure.stonegrinder.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;
import net.nightfallclosure.stonegrinder.block.entity.GrinderBlockEntity;

public class ModBlockEntities {
    public static BlockEntityType<GrinderBlockEntity> GRINDER_BLOCK_ENTITY_TYPE;

    private static BlockEntityType<GrinderBlockEntity> registerBlockEntity(String name, Block block) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(StoneGrinderMod.MOD_ID, name),
                FabricBlockEntityTypeBuilder.create(GrinderBlockEntity::new,
                        block).build(null));
    }

    public static void registerModBlockEntities() {
        GRINDER_BLOCK_ENTITY_TYPE = registerBlockEntity("grinder", ModBlocks.GRINDER_BLOCK);
    }
}
