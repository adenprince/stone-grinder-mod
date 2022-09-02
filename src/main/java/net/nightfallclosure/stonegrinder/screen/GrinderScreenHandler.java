package net.nightfallclosure.stonegrinder.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.FurnaceFuelSlot;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.nightfallclosure.stonegrinder.mixin.AbstractFurnaceScreenHandlerAccessor;
import net.nightfallclosure.stonegrinder.registry.ModRecipes;
import net.nightfallclosure.stonegrinder.registry.ModScreenHandlers;

public class GrinderScreenHandler extends AbstractFurnaceScreenHandler {
    // TODO: Don't use blast furnace recipe book category
    public GrinderScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.GRINDER_SCREEN_HANDLER_TYPE, ModRecipes.GRINDING_RECIPE_TYPE,
                RecipeBookCategory.BLAST_FURNACE, syncId, playerInventory);

        addGrinderSlots(playerInventory);
    }

    public GrinderScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.GRINDER_SCREEN_HANDLER_TYPE, ModRecipes.GRINDING_RECIPE_TYPE,
                RecipeBookCategory.BLAST_FURNACE, syncId, playerInventory, inventory, propertyDelegate);

        addGrinderSlots(playerInventory);
    }

    private void addGrinderSlots(PlayerInventory playerInventory) {
        Inventory inventory = ((AbstractFurnaceScreenHandlerAccessor)this).getInventory();
        Slot slot0 = new Slot(inventory, 0, 56, 53);
        Slot slot1 = new FurnaceFuelSlot(this, inventory, 1, 38, 35);
        Slot slot2 = new FurnaceOutputSlot(playerInventory.player, inventory, 2, 116, 35);

        slot0.id = 0;
        slot1.id = 1;
        slot2.id = 2;

        this.slots.set(0, slot0);
        this.slots.set(1, slot1);
        this.slots.set(2, slot2);
    }
}
