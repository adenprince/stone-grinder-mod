package net.nightfallclosure.stonegrinder.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nightfallclosure.stonegrinder.StoneGrinder;

import static net.nightfallclosure.stonegrinder.block.ModBlocks.GRINDER_BLOCK;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, StoneGrinder.MOD_ID);

    public static final RegistryObject<Item> GRINDER_BLOCK_ITEM = ITEMS.register("grinder",
            () -> new BlockItem(GRINDER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
