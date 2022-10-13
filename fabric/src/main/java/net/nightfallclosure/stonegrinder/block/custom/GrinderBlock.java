package net.nightfallclosure.stonegrinder.block.custom;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nightfallclosure.stonegrinder.block.entity.GrinderBlockEntity;
import net.nightfallclosure.stonegrinder.registry.ModBlockEntities;
import net.nightfallclosure.stonegrinder.registry.ModStats;
import org.jetbrains.annotations.Nullable;

public class GrinderBlock extends AbstractFurnaceBlock {
    public static final IntProperty GRINDER_ANIMATION_FRAME = IntProperty.of(
            "grinder_animation_frame", 0, 4);

    public GrinderBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(GRINDER_ANIMATION_FRAME, GrinderBlockEntity.defaultFrame));
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GrinderBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)((Object)blockEntity));
            player.incrementStat(ModStats.INTERACT_WITH_GRINDER);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GrinderBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntities.GRINDER_BLOCK_ENTITY_TYPE,
                GrinderBlockEntity::serverTick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(GRINDER_ANIMATION_FRAME);
    }
}
