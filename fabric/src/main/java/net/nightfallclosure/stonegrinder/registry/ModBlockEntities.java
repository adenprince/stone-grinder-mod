package net.nightfallclosure.stonegrinder.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;
import net.nightfallclosure.stonegrinder.block.entity.GrinderBlockEntity;

import static net.nightfallclosure.stonegrinder.registry.ModBlocks.GRINDER_BLOCK;

public class ModBlockEntities {
    public static BlockEntityType<GrinderBlockEntity> GRINDER_BLOCK_ENTITY_TYPE;

    public static void registerModBlockEntities() {
        GRINDER_BLOCK_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(StoneGrinderMod.MOD_ID, "grinder"),
                FabricBlockEntityTypeBuilder.create(GrinderBlockEntity::new,
                        GRINDER_BLOCK).build(null));
    }
}
