package net.nightfallclosure.stonegrinder;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.nightfallclosure.stonegrinder.block.ModBlocks;
import net.nightfallclosure.stonegrinder.block.entity.ModBlockEntities;
import net.nightfallclosure.stonegrinder.item.ModItems;
import net.nightfallclosure.stonegrinder.recipe.ModRecipes;
import net.nightfallclosure.stonegrinder.screen.GrinderScreen;
import net.nightfallclosure.stonegrinder.screen.ModMenuTypes;
import org.slf4j.Logger;

@Mod(StoneGrinder.MOD_ID)
public class StoneGrinder
{
    public static final String MOD_ID = "stonegrinder";
    private static final Logger LOGGER = LogUtils.getLogger();

    public StoneGrinder()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.GRINDER_MENU_TYPE.get(), GrinderScreen::new);
        }
    }
}
