package net.nightfallclosure.stonegrinder.rei.plugin.common;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.RecipeBookGridMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import me.shedaniel.rei.forge.REIPlugin;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultCookingDisplay;
import net.nightfallclosure.stonegrinder.rei.plugin.common.displays.cooking.DefaultGrindingDisplay;
import net.nightfallclosure.stonegrinder.screen.GrinderMenu;

@REIPlugin
public class StoneGrinderREIPlugin implements REIServerPlugin {
    public static final CategoryIdentifier<DefaultGrindingDisplay> GRINDING =
            CategoryIdentifier.of("stonegrinder", "grinding");

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(GRINDING, DefaultCookingDisplay.serializer(DefaultGrindingDisplay::new));
    }

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(GRINDING, GrinderMenu.class,
                SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
    }
}
