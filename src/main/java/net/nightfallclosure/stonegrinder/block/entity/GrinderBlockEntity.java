package net.nightfallclosure.stonegrinder.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.BlastFurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.nightfallclosure.stonegrinder.registry.ModBlockEntities;

public class GrinderBlockEntity extends AbstractFurnaceBlockEntity {
    public GrinderBlockEntity(BlockPos pos, BlockState state) {
        // TODO: Use custom recipe type
        super(ModBlockEntities.GRINDER_BLOCK_ENTITY_TYPE, pos, state, RecipeType.BLASTING);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.stonegrinder.grinder");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        // TODO: Use custom screen handler
        return new BlastFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
