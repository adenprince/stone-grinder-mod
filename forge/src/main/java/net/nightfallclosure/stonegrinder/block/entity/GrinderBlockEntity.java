package net.nightfallclosure.stonegrinder.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.nightfallclosure.stonegrinder.recipe.ModRecipes;
import net.nightfallclosure.stonegrinder.screen.GrinderMenu;

public class GrinderBlockEntity extends AbstractFurnaceBlockEntity {
    public GrinderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GRINDER.get(), pPos, pBlockState, ModRecipes.GRINDING_RECIPE_TYPE.get());
    }

    protected Component getDefaultName() {
        return Component.translatable("stonegrinder.container.grinder");
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new GrinderMenu(pId, pPlayer, this, this.dataAccess);
    }
}
