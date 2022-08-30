package net.nightfallclosure.stonegrinder.registry;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.nightfallclosure.stonegrinder.screen.GrinderScreen;
import net.nightfallclosure.stonegrinder.screen.GrinderScreenHandler;

public class ModScreenHandlers {
    public static ScreenHandlerType<GrinderScreenHandler> GRINDER_SCREEN_HANDLER_TYPE;

    public static void registerScreenHandlers() {
        GRINDER_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>(GrinderScreenHandler::new);
    }

    // This method should only be called client-side
    public static void registerScreens() {
        HandledScreens.register(GRINDER_SCREEN_HANDLER_TYPE, GrinderScreen::new);
    }
}
