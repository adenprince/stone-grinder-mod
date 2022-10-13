package net.nightfallclosure.stonegrinder.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nightfallclosure.stonegrinder.mixin.AbstractFurnaceBlockEntityAccessor;
import net.nightfallclosure.stonegrinder.mixin.AbstractFurnaceBlockEntityInvoker;
import net.nightfallclosure.stonegrinder.registry.ModBlockEntities;
import net.nightfallclosure.stonegrinder.registry.ModRecipes;
import net.nightfallclosure.stonegrinder.registry.ModSounds;
import net.nightfallclosure.stonegrinder.screen.GrinderScreenHandler;

import java.util.Arrays;
import java.util.LinkedList;

import static net.nightfallclosure.stonegrinder.block.custom.GrinderBlock.GRINDER_ANIMATION_FRAME;
import static net.nightfallclosure.stonegrinder.constants.GrindingParticleConstants.*;

public class GrinderBlockEntity extends AbstractFurnaceBlockEntity {
    private static final int highestPositionFrame = 0;
    private static final int grindingFrame = 4;
    public static final int defaultFrame = 3; // Used in GrinderBlock class

    private static final double particleStartingPointRandomOffsetMagnitude = 0.25D;
    private static final double particleDeltaRandomOffsetMagnitude = 2.0D;
    private static final double doNotSpawnParticleProbability = 0.125D;

    private boolean grindingOnPreviousTick;
    private LinkedList<Integer> animationFrames;
    private int currentFrame;
    private boolean grindingSoundPlayed;

    public GrinderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRINDER_BLOCK_ENTITY_TYPE, pos, state, ModRecipes.GRINDING_RECIPE_TYPE);

        this.grindingOnPreviousTick = false;
        this.grindingSoundPlayed = true;
        animationFrames = new LinkedList<>();
        currentFrame = defaultFrame;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("stonegrinder.container.grinder");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new GrinderScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, GrinderBlockEntity blockEntity) {
        AbstractFurnaceBlockEntity.tick(world, pos, state, blockEntity);

        ItemStack grindingItemStack = blockEntity.getStack(0);

        boolean blockEntityIsGrinding = blockEntity.isGrinding();

        if (blockEntityIsGrinding) {
            // Grinding started on the current tick or grinding new block
            if (!blockEntity.grindingOnPreviousTick ||
                    ((AbstractFurnaceBlockEntityAccessor)blockEntity).getCookTime() == 0) {
                blockEntity.grindingSoundPlayed = false;

                blockEntity.animationFrames.clear();
                blockEntity.queueNewBlockAnimation();
            }
        }
        else if (blockEntity.getLastFrame() != defaultFrame) {
            blockEntity.animationFrames.clear();
            blockEntity.queueTransitionToDefaultFrame();
        }

        blockEntity.tickAnimation(world, pos, state);

        if (blockEntityIsGrinding && blockEntity.currentFrame == grindingFrame && !grindingItemStack.isEmpty()) {
            blockEntity.tickGrindingParticles(grindingItemStack);

            if (!blockEntity.grindingSoundPlayed) {
                world.playSound(null, pos, ModSounds.GRINDER_GRIND_SOUND_EVENT,
                        SoundCategory.BLOCKS, 1F, 1F);
                blockEntity.grindingSoundPlayed = true;
            }
        }

        blockEntity.grindingOnPreviousTick = blockEntityIsGrinding;
    }

    private int getLastFrame() {
        return this.animationFrames.isEmpty() ? this.currentFrame : this.animationFrames.getLast();
    }

    private void queueNewBlockAnimation() {
        this.queueTransitionToHighestPositionFrame();
        this.queueMultipleOfOneFrame(highestPositionFrame, 7);
        this.queueTransitionToGrindingFrame();
    }

    private void queueTransitionToDefaultFrame() {
        if (this.currentFrame < defaultFrame) {
            for (int i = this.currentFrame + 1; i <= defaultFrame; ++i) {
                this.animationFrames.add(i);
            }
        }
        else {
            for (int i = this.currentFrame - 1; i >= defaultFrame; --i) {
                this.animationFrames.add(i);
            }
        }
    }

    private void queueTransitionToHighestPositionFrame() {
        for (int i = this.getLastFrame() - 1; i >= highestPositionFrame; --i) {
            this.animationFrames.add(i);
        }
    }

    private void queueTransitionToGrindingFrame() {
        for (int i = this.getLastFrame() + 1; i <= grindingFrame; ++i) {
            this.animationFrames.add(i);
        }
    }

    private void queueMultipleOfOneFrame(int frame, int amount) {
        for (int i = 0; i < amount; ++i) {
            this.animationFrames.add(frame);
        }
    }

    private void tickAnimation(World world, BlockPos pos, BlockState state) {
        if (!this.animationFrames.isEmpty()) {
            int nextFrame = this.animationFrames.removeFirst();
            world.setBlockState(pos, state.with(GRINDER_ANIMATION_FRAME, nextFrame));
            this.currentFrame = nextFrame;
        }
    }

    private void tickGrindingParticles(ItemStack grindingItemStack) {
        // A copy is made to prevent a missing particle texture during slowdown
        ItemStack grindingItemStackCopy = grindingItemStack.copy();
        ItemStackParticleEffect grindingParticleEffect = new ItemStackParticleEffect(ParticleTypes.ITEM,
                grindingItemStackCopy);

        ServerWorld serverWorld = (ServerWorld)world;

        if (serverWorld.random.nextDouble() > doNotSpawnParticleProbability) {
            spawnGrinderParticles(serverWorld, pos, grindingParticleEffect);
        }
    }

    private static void spawnGrinderParticles(ServerWorld serverWorld, BlockPos pos,
                                              ItemStackParticleEffect grindingParticleEffect) {
        double randomDouble = serverWorld.random.nextDouble() - 0.5D;

        double particleStartingPointRandomOffset = randomDouble * particleStartingPointRandomOffsetMagnitude;
        double particleDeltaRandomOffset = randomDouble * particleDeltaRandomOffsetMagnitude;

        for (int i = 0; i < 4; ++i) {
            double particleXRandomOffset = particleXZRandomOffsetVectors[i][0] * particleStartingPointRandomOffset;
            double particleZRandomOffset = particleXZRandomOffsetVectors[i][1] * particleStartingPointRandomOffset;

            double particleXDeltaRandomOffset = particleXZRandomOffsetVectors[i][0] * particleDeltaRandomOffset;
            double particleZDeltaRandomOffset = particleXZRandomOffsetVectors[i][1] * particleDeltaRandomOffset;

            serverWorld.spawnParticles(grindingParticleEffect,
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
        boolean ingredientSlotIsNotEmpty = !this.inventory.get(0).isEmpty();
        boolean fuelSlotIsNotEmpty = !this.inventory.get(1).isEmpty();
        boolean isBurning = this.propertyDelegate.get(0) > 0;

        RecipeManager.MatchGetter<Inventory, ? extends AbstractCookingRecipe> grinderMatchGetter =
                ((AbstractFurnaceBlockEntityAccessor)this).getMatchGetter();

        Recipe recipe = ingredientSlotIsNotEmpty ?
                grinderMatchGetter.getFirstMatch(this, world).orElse(null) : null;

        return (isBurning || (ingredientSlotIsNotEmpty && fuelSlotIsNotEmpty)) &&
                AbstractFurnaceBlockEntityInvoker.invokeCanAcceptRecipeOutput(recipe,
                        this.inventory, this.getMaxCountPerStack());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.grindingOnPreviousTick = nbt.getBoolean("GrindingOnPreviousTick");
        this.animationFrames = new LinkedList<>(
                Arrays.stream(nbt.getIntArray("AnimationFrames")).boxed().toList());
        this.currentFrame = nbt.getShort("CurrentFrame");
        this.grindingSoundPlayed = nbt.getBoolean("GrindingSoundPlayed");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("GrindingOnPreviousTick", this.grindingOnPreviousTick);
        nbt.putIntArray("AnimationFrames", this.animationFrames.stream().mapToInt(x -> x).toArray());
        nbt.putShort("CurrentFrame", (short)this.currentFrame);
        nbt.putBoolean("GrindingSoundPlayed", this.grindingSoundPlayed);
    }
}
