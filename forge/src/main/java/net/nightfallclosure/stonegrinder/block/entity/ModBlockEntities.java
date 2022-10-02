package net.nightfallclosure.stonegrinder.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nightfallclosure.stonegrinder.StoneGrinder;
import net.nightfallclosure.stonegrinder.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, StoneGrinder.MOD_ID);

    public static final RegistryObject<BlockEntityType<GrinderBlockEntity>> GRINDER =
            BLOCK_ENTITIES.register("grinder", () ->
                    BlockEntityType.Builder.of(GrinderBlockEntity::new,
                            ModBlocks.GRINDER_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}