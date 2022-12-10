package net.nightfallclosure.stonegrinder.registry;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;
import net.nightfallclosure.stonegrinder.recipe.GrindingRecipe;
import net.nightfallclosure.stonegrinder.recipe.GrindingRecipeSerializer;

public class ModRecipes {
    public static RecipeType<GrindingRecipe> GRINDING_RECIPE_TYPE;
    public static GrindingRecipeSerializer GRINDING_RECIPE_SERIALIZER;

    public static void registerRecipes() {
        GRINDING_RECIPE_TYPE = RecipeType.register(StoneGrinderMod.MOD_ID + ":grinding");
        GRINDING_RECIPE_SERIALIZER = RecipeSerializer.register(StoneGrinderMod.MOD_ID + ":grinding",
                new GrindingRecipeSerializer(GrindingRecipe::new, 100));
    }
}
