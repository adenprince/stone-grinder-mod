package net.nightfallclosure.stonegrinder.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
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

import java.util.LinkedList;

import static net.nightfallclosure.stonegrinder.block.custom.GrinderBlock.GRINDER_ANIMATION_FRAME;

public class GrinderBlockEntity extends AbstractFurnaceBlockEntity {
    private static final int highestPositionFrame = 0;
    private static final int grindingFrame = 9;
    public static final int defaultFrame = 6; // Used in GrinderBlock class

    private static final int grindingParticleTimerThreshold = 2;
    private static final int maxSoundTimerThreshold = 241; // Exclusive bound
    private static final int minSoundTimerThreshold = 140; // Inclusive bound

    private int grindingParticleTimer;
    private int grindingSoundTimer;
    private boolean grindingOnPreviousTick;
    private final LinkedList<Integer> animationFrames;
    private int currentFrame;

    public GrinderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRINDER_BLOCK_ENTITY_TYPE, pos, state, ModRecipes.GRINDING_RECIPE_TYPE);

        // TODO: Fix instance variables getting reset when world is started
        this.grindingParticleTimer = 0;
        this.grindingSoundTimer = 0;
        this.grindingOnPreviousTick = false;
        animationFrames = new LinkedList<>();
        currentFrame = defaultFrame;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.stonegrinder.grinder");
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
            if (!blockEntity.grindingOnPreviousTick) {
                // Grinding started on the current tick
                blockEntity.grindingSoundTimer = 0;
                blockEntity.grindingParticleTimer = 0;

                blockEntity.animationFrames.clear();
                blockEntity.queueNewBlockAnimation();
            }
            else if (((AbstractFurnaceBlockEntityAccessor)blockEntity).getCookTime() == 0) {
                // Grinding new block
                blockEntity.grindingParticleTimer = 0;

                blockEntity.animationFrames.clear();
                blockEntity.queueNewBlockAnimation();
            }

            if (blockEntity.currentFrame == grindingFrame && !grindingItemStack.isEmpty()) {
                blockEntity.tickGrindingParticles(grindingItemStack);
                blockEntity.tickGrindingSound();
            }
        }
        else if (blockEntity.getLastFrame() != defaultFrame) {
            blockEntity.animationFrames.clear();
            blockEntity.queueTransitionToDefaultFrame();
        }

        blockEntity.tickAnimation(world, pos, state);

        blockEntity.grindingOnPreviousTick = blockEntityIsGrinding;
    }

    private int getLastFrame() {
        return this.animationFrames.isEmpty() ? this.currentFrame : this.animationFrames.getLast();
    }

    private void queueNewBlockAnimation() {
        this.queueTransitionToHighestPositionFrame();
        this.queueMultipleOfOneFrame(highestPositionFrame, 5);
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
        int nextFrame = this.animationFrames.isEmpty() ? this.currentFrame : this.animationFrames.removeFirst();

        world.setBlockState(pos, state.with(GRINDER_ANIMATION_FRAME, nextFrame));

        this.currentFrame = nextFrame;
    }

    private void tickGrindingSound() {
        if (--this.grindingSoundTimer <= 0) {
            world.playSound(null, pos, ModSounds.GRINDER_GRIND_SOUND_EVENT,
                    SoundCategory.BLOCKS, 1F, 1F);
            this.grindingSoundTimer = nextGrindingSoundTimerThreshold(world);
        }
    }

    private void tickGrindingParticles(ItemStack grindingItemStack) {
        if (--this.grindingParticleTimer <= 0) {
            // A copy is made to prevent a missing particle texture during slowdown
            // TODO: Investigate this issue
            ItemStack grindingItemStackCopy = grindingItemStack.copy();
            ItemStackParticleEffect grindingParticleEffect = new ItemStackParticleEffect(ParticleTypes.ITEM,
                    grindingItemStackCopy);
            spawnGrinderParticles((ServerWorld) world, pos, grindingParticleEffect);

            this.grindingParticleTimer = grindingParticleTimerThreshold;
        }
    }

    private static int nextGrindingSoundTimerThreshold(World world) {
        return world.random.nextInt(maxSoundTimerThreshold - minSoundTimerThreshold) + minSoundTimerThreshold;
    }

    private static final double[][] particleXZOffsets = {
            {0.0D, 0.5D},
            {1.0D, 0.5D},
            {0.5D, 0.0D},
            {0.5D, 1.0D}
    };

    private static final double[][] particleXZDeltas = {
            {-2.0D, 0.0D},
            {2.0D, 0.0D},
            {0.0D, -2.0D},
            {0.0D, 2.0D}
    };

    private static void spawnGrinderParticles(ServerWorld serverWorld, BlockPos pos,
                                       ItemStackParticleEffect grindingParticleEffect) {
        for (int i = 0; i < 4; ++i) {
            serverWorld.spawnParticles(grindingParticleEffect,
                    pos.getX() + particleXZOffsets[i][0],
                    pos.getY() + 0.3D,
                    pos.getZ() + particleXZOffsets[i][1],
                    0,
                    particleXZDeltas[i][0],
                    0.5D,
                    particleXZDeltas[i][1],
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
                grinderMatchGetter.getFirstMatch(this, world).orElse(null) :
                null;

        return (isBurning || (ingredientSlotIsNotEmpty && fuelSlotIsNotEmpty)) &&
                AbstractFurnaceBlockEntityInvoker.invokeCanAcceptRecipeOutput(recipe,
                        this.inventory, this.getMaxCountPerStack());
    }
}
