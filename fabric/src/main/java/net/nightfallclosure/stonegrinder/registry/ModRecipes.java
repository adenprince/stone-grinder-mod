package net.nightfallclosure.stonegrinder.registry;

import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;
import net.nightfallclosure.stonegrinder.recipe.GrindingRecipe;

public class ModRecipes {
    public static RecipeType<GrindingRecipe> GRINDING_RECIPE_TYPE;
    public static CookingRecipeSerializer<GrindingRecipe> GRINDING_RECIPE_SERIALIZER;

    public static void registerRecipes() {
        GRINDING_RECIPE_TYPE = RecipeType.register(StoneGrinderMod.MOD_ID + ":grinding");
        GRINDING_RECIPE_SERIALIZER = RecipeSerializer.register(StoneGrinderMod.MOD_ID + ":grinding",
                new CookingRecipeSerializer<>(GrindingRecipe::new, 100));
    }
}
