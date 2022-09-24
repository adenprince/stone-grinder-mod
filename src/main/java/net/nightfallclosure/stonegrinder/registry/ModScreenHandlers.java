package net.nightfallclosure.stonegrinder.registry;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;
import net.nightfallclosure.stonegrinder.screen.GrinderScreen;
import net.nightfallclosure.stonegrinder.screen.GrinderScreenHandler;

public class ModScreenHandlers {
    public static ScreenHandlerType<GrinderScreenHandler> GRINDER_SCREEN_HANDLER_TYPE;

    public static void registerScreenHandlers() {
        GRINDER_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>(GrinderScreenHandler::new);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(StoneGrinderMod.MOD_ID, "grinder"),
                GRINDER_SCREEN_HANDLER_TYPE);
    }

    // This method should only be called client-side
    public static void registerScreens() {
        HandledScreens.register(GRINDER_SCREEN_HANDLER_TYPE, GrinderScreen::new);
    }
}
