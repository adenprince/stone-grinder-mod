package net.nightfallclosure.stonegrinder;

import net.fabricmc.api.ClientModInitializer;
import net.nightfallclosure.stonegrinder.registry.ModScreenHandlers;

public class StoneGrinderModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModScreenHandlers.registerScreens();
    }
}
