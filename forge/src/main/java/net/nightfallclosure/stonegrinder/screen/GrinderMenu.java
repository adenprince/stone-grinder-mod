package net.nightfallclosure.stonegrinder.screen;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.nightfallclosure.stonegrinder.recipe.ModRecipes;

public class GrinderMenu extends AbstractFurnaceMenu {
    public GrinderMenu(int pContainerId, Inventory pPlayerInventory) {
        super(ModMenuTypes.GRINDER_MENU_TYPE.get(), ModRecipes.GRINDING_RECIPE_TYPE.get(),
                RecipeBookType.BLAST_FURNACE, pContainerId, pPlayerInventory);

        addGrinderSlots(pPlayerInventory);
    }

    public GrinderMenu(int pContainerId, Inventory pPlayerInventory, Container pGrinderContainer,
                       ContainerData pGrinderData) {
        super(ModMenuTypes.GRINDER_MENU_TYPE.get(), ModRecipes.GRINDING_RECIPE_TYPE.get(),
                RecipeBookType.BLAST_FURNACE, pContainerId, pPlayerInventory, pGrinderContainer, pGrinderData);

        addGrinderSlots(pPlayerInventory);
    }

    private void addGrinderSlots(Inventory pPlayerInventory) {
        Slot slot0 = new Slot(this.container, 0, 56, 53);
        Slot slot1 = new FurnaceFuelSlot(this, this.container, 1, 38, 35);
        Slot slot2 = new FurnaceResultSlot(pPlayerInventory.player, this.container, 2, 116, 35);

        slot0.index = 0;
        slot1.index = 1;
        slot2.index = 2;

        this.slots.set(0, slot0);
        this.slots.set(1, slot1);
        this.slots.set(2, slot2);
    }
}
