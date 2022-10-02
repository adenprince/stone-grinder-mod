package net.nightfallclosure.stonegrinder.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;

public class GrinderBlock extends AbstractFurnaceBlock {
    public static final IntegerProperty GRINDER_ANIMATION_FRAME = IntegerProperty.create(
            "grinder_animation_frame", 0, 4);

    public GrinderBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(GRINDER_ANIMATION_FRAME, Integer.valueOf(3)));
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        // TODO: Use new block entity
        return new BlastFurnaceBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
                                                                  BlockEntityType<T> pBlockEntityType) {
        return createFurnaceTicker(pLevel, pBlockEntityType, BlockEntityType.BLAST_FURNACE);
    }

    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof BlastFurnaceBlockEntity) {
            pPlayer.openMenu((MenuProvider)blockentity);

            // TODO: Add grinder interaction stat
            //pPlayer.awardStat(Stats.INTERACT_WITH_BLAST_FURNACE);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(GRINDER_ANIMATION_FRAME);
    }
}
