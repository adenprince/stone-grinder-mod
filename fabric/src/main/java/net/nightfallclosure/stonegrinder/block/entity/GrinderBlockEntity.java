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
import static net.nightfallclosure.stonegrinder.constants.GrinderAnimationConstants.*;
import static net.nightfallclosure.stonegrinder.constants.GrindingParticleConstants.*;

public class GrinderBlockEntity extends AbstractFurnaceBlockEntity {
    private boolean grindingOnPreviousTick;
    private boolean grindingSoundPlayed;
    private final GrinderAnimator grinderAnimator;

    public GrinderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRINDER_BLOCK_ENTITY_TYPE, pos, state, ModRecipes.GRINDING_RECIPE_TYPE);

        this.grindingOnPreviousTick = false;
        this.grindingSoundPlayed = true;
        this.grinderAnimator = new GrinderAnimator();
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

                blockEntity.grinderAnimator.getAnimationFrames().clear();
                blockEntity.grinderAnimator.queueNewBlockAnimation();
            }
        }
        else if (blockEntity.grinderAnimator.getLastFrame() != defaultFrame) {
            blockEntity.grinderAnimator.getAnimationFrames().clear();
            blockEntity.grinderAnimator.queueTransitionToDefaultFrame();
        }

        blockEntity.tickAnimation(world, pos, state);

        if (blockEntityIsGrinding && blockEntity.grinderAnimator.getCurrentFrame() == grindingFrame &&
                !grindingItemStack.isEmpty()) {
            blockEntity.tickGrindingParticles(grindingItemStack);

            if (!blockEntity.grindingSoundPlayed) {
                world.playSound(null, pos, ModSounds.GRINDER_GRIND_SOUND_EVENT,
                        SoundCategory.BLOCKS, 1F, 1F);
                blockEntity.grindingSoundPlayed = true;
            }
        }

        blockEntity.grindingOnPreviousTick = blockEntityIsGrinding;
    }

    private void tickAnimation(World world, BlockPos pos, BlockState state) {
        if (!this.grinderAnimator.getAnimationFrames().isEmpty()) {
            int nextFrame = this.grinderAnimator.getAnimationFrames().removeFirst();
            world.setBlockState(pos, state.with(GRINDER_ANIMATION_FRAME, nextFrame));
            this.grinderAnimator.setCurrentFrame(nextFrame);
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
        this.grinderAnimator.setAnimationFrames(new LinkedList<>(
                Arrays.stream(nbt.getIntArray("AnimationFrames")).boxed().toList()));
        this.grinderAnimator.setCurrentFrame(nbt.getShort("CurrentFrame"));
        this.grindingSoundPlayed = nbt.getBoolean("GrindingSoundPlayed");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("GrindingOnPreviousTick", this.grindingOnPreviousTick);
        nbt.putIntArray("AnimationFrames", this.grinderAnimator.getAnimationFrames()
                .stream().mapToInt(x -> x).toArray());
        nbt.putShort("CurrentFrame", (short)this.grinderAnimator.getCurrentFrame());
        nbt.putBoolean("GrindingSoundPlayed", this.grindingSoundPlayed);
    }
}
