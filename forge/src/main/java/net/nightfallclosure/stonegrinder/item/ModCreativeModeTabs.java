package net.nightfallclosure.stonegrinder.item;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.nightfallclosure.stonegrinder.StoneGrinder;

@Mod.EventBusSubscriber(modid = StoneGrinder.MOD_ID, bus = Bus.MOD)
public class ModCreativeModeTabs {
    @SubscribeEvent
    public static void buildContents(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.GRINDER_BLOCK_ITEM);
        }
    }
}
