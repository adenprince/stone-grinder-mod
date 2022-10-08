package net.nightfallclosure.stonegrinder.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nightfallclosure.stonegrinder.StoneGrinder;
import net.nightfallclosure.stonegrinder.block.custom.GrinderBlock;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, StoneGrinder.MOD_ID);

    public static final RegistryObject<Block> GRINDER_BLOCK = registerBlock("grinder",
            () -> new GrinderBlock(BlockBehaviour.Properties
                    .of(Material.STONE)
                    .strength(3.5f, 17.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> result = BLOCKS.register(name, block);
        return result;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}