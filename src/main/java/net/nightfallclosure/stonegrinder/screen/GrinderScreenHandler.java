package net.nightfallclosure.stonegrinder.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.nightfallclosure.stonegrinder.registry.ModRecipes;
import net.nightfallclosure.stonegrinder.registry.ModScreenHandlers;

public class GrinderScreenHandler extends AbstractFurnaceScreenHandler {
    // TODO: Don't use blast furnace recipe book category
    public GrinderScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.GRINDER_SCREEN_HANDLER_TYPE, ModRecipes.GRINDING_RECIPE_TYPE,
                RecipeBookCategory.BLAST_FURNACE, syncId, playerInventory);
    }

    public GrinderScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.GRINDER_SCREEN_HANDLER_TYPE, ModRecipes.GRINDING_RECIPE_TYPE,
                RecipeBookCategory.BLAST_FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }
}
