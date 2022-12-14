package net.nightfallclosure.stonegrinder.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.nightfallclosure.stonegrinder.recipe.ModRecipes;
import net.nightfallclosure.stonegrinder.screen.GrinderMenu;
import net.nightfallclosure.stonegrinder.sound.ModSounds;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;

import static net.nightfallclosure.stonegrinder.block.custom.GrinderBlock.GRINDER_ANIMATION_FRAME;
import static net.nightfallclosure.stonegrinder.constants.GrinderAnimationConstants.*;
import static net.nightfallclosure.stonegrinder.constants.GrindingParticleConstants.*;

public class GrinderBlockEntity extends AbstractFurnaceBlockEntity {
    private boolean grindingOnPreviousTick;
    private boolean grindingSoundPlayed;
    private final GrinderAnimator grinderAnimator;

    public GrinderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GRINDER.get(), pPos, pBlockState, ModRecipes.GRINDING_RECIPE_TYPE.get());

        this.grindingOnPreviousTick = false;
        this.grindingSoundPlayed = true;
        this.grinderAnimator = new GrinderAnimator();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("stonegrinder.container.grinder");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new GrinderMenu(pId, pPlayer, this, this.dataAccess);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState,
                                  GrinderBlockEntity pBlockEntity) {
        AbstractFurnaceBlockEntity.serverTick(pLevel, pPos, pState, pBlockEntity);

        ItemStack grindingItemStack = pBlockEntity.getItem(0);

        boolean blockEntityIsGrinding = pBlockEntity.isGrinding();

        if (blockEntityIsGrinding) {
            // Grinding started on the current tick or grinding new block
            if (!pBlockEntity.grindingOnPreviousTick ||
                    pBlockEntity.cookingProgress == 0) {
                pBlockEntity.grindingSoundPlayed = false;

                pBlockEntity.grinderAnimator.getAnimationFrames().clear();
                pBlockEntity.grinderAnimator.queueNewBlockAnimation();
            }
        }
        else if (pBlockEntity.grinderAnimator.getLastFrame() != defaultFrame) {
            pBlockEntity.grinderAnimator.getAnimationFrames().clear();
            pBlockEntity.grinderAnimator.queueTransitionToDefaultFrame();
        }

        pBlockEntity.tickAnimation(pLevel, pPos, pState);

        if (blockEntityIsGrinding && pBlockEntity.grinderAnimator.getCurrentFrame() == grindingFrame &&
                !grindingItemStack.isEmpty()) {
            pBlockEntity.tickGrindingParticles(grindingItemStack);

            if (!pBlockEntity.grindingSoundPlayed) {
                pLevel.playSound(null, pPos, ModSounds.GRINDER_GRIND_SOUND_EVENT.get(),
                        SoundSource.BLOCKS, 1F, 1F);
                pBlockEntity.grindingSoundPlayed = true;
            }
        }

        pBlockEntity.grindingOnPreviousTick = blockEntityIsGrinding;
    }

    private void tickAnimation(Level pLevel, BlockPos pPos, BlockState pState) {
        if (!this.grinderAnimator.getAnimationFrames().isEmpty()) {
            int nextFrame = this.grinderAnimator.getAnimationFrames().removeFirst();
            pLevel.setBlockAndUpdate(pPos, pState.setValue(GRINDER_ANIMATION_FRAME, nextFrame));
            this.grinderAnimator.setCurrentFrame(nextFrame);
        }
    }

    private void tickGrindingParticles(ItemStack grindingItemStack) {
        // A copy is made to prevent a missing particle texture during slowdown
        ItemStack grindingItemStackCopy = grindingItemStack.copy();
        ItemParticleOption grindingParticleOption = new ItemParticleOption(ParticleTypes.ITEM,
                grindingItemStackCopy);

        ServerLevel serverLevel = (ServerLevel)level;

        if (serverLevel.random.nextDouble() > doNotSpawnParticleProbability) {
            spawnGrinderParticles(serverLevel, worldPosition, grindingParticleOption);
        }
    }

    private static void spawnGrinderParticles(ServerLevel serverLevel, BlockPos pos,
                                              ItemParticleOption grindingParticleOption) {
        double randomDouble = serverLevel.random.nextDouble() - 0.5D;

        double particleStartingPointRandomOffset = randomDouble * particleStartingPointRandomOffsetMagnitude;
        double particleDeltaRandomOffset = randomDouble * particleDeltaRandomOffsetMagnitude;

        for (int i = 0; i < 4; ++i) {
            double particleXRandomOffset = particleXZRandomOffsetVectors[i][0] * particleStartingPointRandomOffset;
            double particleZRandomOffset = particleXZRandomOffsetVectors[i][1] * particleStartingPointRandomOffset;

            double particleXDeltaRandomOffset = particleXZRandomOffsetVectors[i][0] * particleDeltaRandomOffset;
            double particleZDeltaRandomOffset = particleXZRandomOffsetVectors[i][1] * particleDeltaRandomOffset;

            serverLevel.sendParticles(grindingParticleOption,
                    pos.getX() + particleXZOffsets[i][0] + particleXRandomOffset,
                    pos.getY() + 0.3D,
                    pos.getZ() + particleXZOffsets[i][1] + particleZRandomOffset,
                    0,
                    particleXZDeltas[i][0] + particleXDeltaRandomOffset,
                    0.5D,
                    particleXZDeltas[i][1] + particleZDeltaRandomOffset,
                    0.05D);
        }
    }

    private boolean isGrinding() {
        boolean ingredientSlotIsNotEmpty = !this.items.get(0).isEmpty();
        boolean fuelSlotIsNotEmpty = !this.items.get(1).isEmpty();
        boolean isBurning = this.dataAccess.get(0) > 0;

        Recipe<Container> recipe = ingredientSlotIsNotEmpty ?
                this.quickCheck.getRecipeFor(this, level).orElse(null) : null;

        return (isBurning || (ingredientSlotIsNotEmpty && fuelSlotIsNotEmpty)) &&
                this.canBurn(recipe, this.items, this.getMaxStackSize());
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.grindingOnPreviousTick = nbt.getBoolean("GrindingOnPreviousTick");
        this.grinderAnimator.setAnimationFrames(new LinkedList<>(
                Arrays.stream(nbt.getIntArray("AnimationFrames")).boxed().toList()));
        this.grinderAnimator.setCurrentFrame(nbt.getShort("CurrentFrame"));
        this.grindingSoundPlayed = nbt.getBoolean("GrindingSoundPlayed");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("GrindingOnPreviousTick", this.grindingOnPreviousTick);
        nbt.putIntArray("AnimationFrames", this.grinderAnimator.getAnimationFrames()
                .stream().mapToInt(x -> x).toArray());
        nbt.putShort("CurrentFrame", (short)this.grinderAnimator.getCurrentFrame());
        nbt.putBoolean("GrindingSoundPlayed", this.grindingSoundPlayed);
    }
}
