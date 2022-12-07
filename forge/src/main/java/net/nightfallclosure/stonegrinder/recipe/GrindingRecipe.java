package net.nightfallclosure.stonegrinder.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.nightfallclosure.stonegrinder.block.ModBlocks;
import org.jetbrains.annotations.NotNull;

public class GrindingRecipe extends AbstractCookingRecipe {
    public GrindingRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult,
                          float pExperience, int pCookingTime) {
        super(ModRecipes.GRINDING_RECIPE_TYPE.get(), pId, pGroup, pIngredient, pResult, pExperience, pCookingTime);
    }

    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.GRINDER_BLOCK.get());
    }

    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.GRINDING_RECIPE_SERIALIZER.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
