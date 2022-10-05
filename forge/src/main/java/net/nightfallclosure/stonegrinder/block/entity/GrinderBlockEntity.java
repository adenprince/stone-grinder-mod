package net.nightfallclosure.stonegrinder.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.nightfallclosure.stonegrinder.recipe.ModRecipes;
import net.nightfallclosure.stonegrinder.screen.GrinderMenu;
import net.nightfallclosure.stonegrinder.sound.ModSounds;

public class GrinderBlockEntity extends AbstractFurnaceBlockEntity {
    private static final double doNotSpawnParticleProbability = 0.125D;

    private boolean grindingOnPreviousTick;

    public GrinderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GRINDER.get(), pPos, pBlockState, ModRecipes.GRINDING_RECIPE_TYPE.get());

        this.grindingOnPreviousTick = false;
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
                pLevel.playSound(null, pPos, ModSounds.GRINDER_GRIND_SOUND_EVENT.get(),
                        SoundSource.BLOCKS, 1f, 1f);
            }

            pBlockEntity.tickGrindingParticles(grindingItemStack);
        }

        pBlockEntity.grindingOnPreviousTick = blockEntityIsGrinding;
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

    private static final double[][] particleXZOffsets = {
            {0.1D, 0.5D},
            {0.9D, 0.5D},
            {0.5D, 0.1D},
            {0.5D, 0.9D}
    };

    private static final double[][] particleXZDeltas = {
            {-2.1D, 0.0D},
            {2.1D, 0.0D},
            {0.0D, -2.1D},
            {0.0D, 2.1D}
    };

    private static void spawnGrinderParticles(ServerLevel serverLevel, BlockPos pos,
                                              ItemParticleOption grindingParticleOption) {
        for (int i = 0; i < 4; ++i) {
            serverLevel.sendParticles(grindingParticleOption,
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
        boolean ingredientSlotIsNotEmpty = !this.items.get(0).isEmpty();
        boolean fuelSlotIsNotEmpty = !this.items.get(1).isEmpty();
        boolean isBurning = this.dataAccess.get(0) > 0;

        RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> grinderQuickCheck =
                this.quickCheck;

        Recipe recipe = ingredientSlotIsNotEmpty ?
                grinderQuickCheck.getRecipeFor(this, level).orElse(null) : null;

        return (isBurning || (ingredientSlotIsNotEmpty && fuelSlotIsNotEmpty)) &&
                this.canBurn(recipe, this.items, this.getMaxStackSize());
    }
}
