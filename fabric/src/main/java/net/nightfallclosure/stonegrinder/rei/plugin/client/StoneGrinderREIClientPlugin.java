package net.nightfallclosure.stonegrinder.rei.plugin.client;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.DefaultClientPlugin;
import net.nightfallclosure.stonegrinder.recipe.GrindingRecipe;
import net.nightfallclosure.stonegrinder.registry.ModBlocks;
import net.nightfallclosure.stonegrinder.registry.ModRecipes;
import net.nightfallclosure.stonegrinder.rei.plugin.client.categories.DefaultGrindingCategory;
import net.nightfallclosure.stonegrinder.rei.plugin.common.displays.cooking.DefaultGrindingDisplay;
import net.nightfallclosure.stonegrinder.screen.GrinderScreen;

import static net.nightfallclosure.stonegrinder.rei.plugin.common.StoneGrinderREIPlugin.GRINDING;

public class StoneGrinderREIClientPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new DefaultGrindingCategory(GRINDING, EntryStacks.of(ModBlocks.GRINDER_BLOCK_ITEM),
                "category.stonegrinder.grinding"));
        registry.addWorkstations(GRINDING, EntryStacks.of(ModBlocks.GRINDER_BLOCK_ITEM));
        registry.addWorkstations(DefaultClientPlugin.FUEL, EntryStacks.of(ModBlocks.GRINDER_BLOCK_ITEM));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(GrindingRecipe.class,
                ModRecipes.GRINDING_RECIPE_TYPE, DefaultGrindingDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(78, 32, 28, 23),
                GrinderScreen.class, GRINDING);
    }
}
