package net.nightfallclosure.stonegrinder.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.nightfallclosure.stonegrinder.block.ModBlocks;

public class GrindingRecipe extends AbstractCookingRecipe {
    public GrindingRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult,
                          float pExperience, int pCookingTime) {
        super(ModRecipes.GRINDING_RECIPE_TYPE.get(), pId, pGroup, pIngredient, pResult, pExperience, pCookingTime);
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.GRINDER_BLOCK.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.GRINDING_RECIPE_SERIALIZER.get();
    }
}
