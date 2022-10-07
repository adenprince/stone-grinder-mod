package net.nightfallclosure.stonegrinder.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.nightfallclosure.stonegrinder.StoneGrinder;
import net.nightfallclosure.stonegrinder.block.ModBlocks;
import net.nightfallclosure.stonegrinder.recipe.GrindingRecipe;
import net.nightfallclosure.stonegrinder.recipe.ModRecipes;
import net.nightfallclosure.stonegrinder.screen.GrinderMenu;
import net.nightfallclosure.stonegrinder.screen.GrinderScreen;
import net.nightfallclosure.stonegrinder.screen.ModMenuTypes;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class StoneGrinderPlugin implements IModPlugin {
    public static RecipeType<GrindingRecipe> GRINDING_RECIPE_TYPE =
            new RecipeType<>(GrindingRecipeCategory.UID, GrindingRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(StoneGrinder.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GrindingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<GrindingRecipe> grindingRecipes = recipeManager.getAllRecipesFor(ModRecipes.GRINDING_RECIPE_TYPE.get());
        registration.addRecipes(GRINDING_RECIPE_TYPE, grindingRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GrinderScreen.class, 78, 32, 28, 23,
                GRINDING_RECIPE_TYPE, RecipeTypes.FUELING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(GrinderMenu.class, ModMenuTypes.GRINDER_MENU_TYPE.get(),
                GRINDING_RECIPE_TYPE, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(GrinderMenu.class, ModMenuTypes.GRINDER_MENU_TYPE.get(),
                RecipeTypes.FUELING, 1, 1, 3, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GRINDER_BLOCK.get()), GRINDING_RECIPE_TYPE,
                RecipeTypes.FUELING);
    }
}
