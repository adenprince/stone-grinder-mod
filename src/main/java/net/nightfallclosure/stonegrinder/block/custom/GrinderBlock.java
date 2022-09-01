package net.nightfallclosure.stonegrinder.block.custom;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nightfallclosure.stonegrinder.block.entity.GrinderBlockEntity;
import net.nightfallclosure.stonegrinder.registry.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class GrinderBlock extends AbstractFurnaceBlock {
    public GrinderBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GrinderBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)((Object)blockEntity));
            // TODO: Add stat?
            //player.incrementStat(Stats.INTERACT_WITH_BLAST_FURNACE);
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
}
