package net.nightfallclosure.stonegrinder.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.nightfallclosure.stonegrinder.registry.ModBlocks;
import net.nightfallclosure.stonegrinder.registry.ModRecipes;

public class GrindingRecipe extends AbstractCookingRecipe {
    public GrindingRecipe(Identifier id, String group, Ingredient input, ItemStack output,
                          float experience, int cookTime) {
        super(ModRecipes.GRINDING_RECIPE_TYPE, id, group, input, output, experience, cookTime);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.GRINDER_BLOCK);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.GRINDING_RECIPE_SERIALIZER;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }
}
