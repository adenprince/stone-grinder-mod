package net.nightfallclosure.stonegrinder.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GrinderBlockEntity extends AbstractFurnaceBlockEntity {
    public GrinderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        // TODO: Use custom recipe type
        super(ModBlockEntities.GRINDER.get(), pPos, pBlockState, RecipeType.BLASTING);
    }

    protected Component getDefaultName() {
        return Component.translatable("stonegrinder.container.grinder");
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        // TODO: Use custom menu
        return new BlastFurnaceMenu(pId, pPlayer, this, this.dataAccess);
    }
}
