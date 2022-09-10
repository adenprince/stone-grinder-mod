package net.nightfallclosure.stonegrinder.rei.plugin.common.displays.cooking;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultCookingDisplay;
import net.minecraft.nbt.NbtCompound;
import net.nightfallclosure.stonegrinder.recipe.GrindingRecipe;
import net.nightfallclosure.stonegrinder.rei.plugin.common.StoneGrinderREIPlugin;

import java.util.List;

public class DefaultGrindingDisplay extends DefaultCookingDisplay {
    public DefaultGrindingDisplay(GrindingRecipe recipe) {
        super(recipe);
    }

    public DefaultGrindingDisplay(List<EntryIngredient> input, List<EntryIngredient> output, NbtCompound tag) {
        super(input, output, tag);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return StoneGrinderREIPlugin.GRINDING;
    }
}
