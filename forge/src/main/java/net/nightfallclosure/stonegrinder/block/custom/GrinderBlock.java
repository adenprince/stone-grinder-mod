package net.nightfallclosure.stonegrinder.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.nightfallclosure.stonegrinder.block.entity.GrinderBlockEntity;
import net.nightfallclosure.stonegrinder.block.entity.ModBlockEntities;
import net.nightfallclosure.stonegrinder.stat.ModStats;

import javax.annotation.Nullable;

import static net.nightfallclosure.stonegrinder.constants.GrinderAnimationConstants.*;

public class GrinderBlock extends AbstractFurnaceBlock {
    public static final IntegerProperty GRINDER_ANIMATION_FRAME = IntegerProperty.create(
            "grinder_animation_frame", highestPositionFrame, grindingFrame);

    public GrinderBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(GRINDER_ANIMATION_FRAME,
                defaultFrame));
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GrinderBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
                                                                  BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ModBlockEntities.GRINDER.get(),
                GrinderBlockEntity::serverTick);
    }

    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof GrinderBlockEntity) {
            pPlayer.openMenu((MenuProvider)blockentity);

            pPlayer.awardStat(ModStats.INTERACT_WITH_GRINDER);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(GRINDER_ANIMATION_FRAME);
    }
}
