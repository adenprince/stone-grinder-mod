package net.nightfallclosure.stonegrinder.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;
import net.nightfallclosure.stonegrinder.recipe.GrindingRecipe;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    private static boolean craftRecipeIsGrindingRecipe;
    private static int correctIncrementAmount;

    @Inject(method = "craftRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;increment(I)V"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void craftRecipeIncrementInject(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots,
                                                   int count, CallbackInfoReturnable<Boolean> info,
                                                   ItemStack itemStack, ItemStack itemStack2, ItemStack itemStack3) {
        if (recipe instanceof GrindingRecipe) {
            craftRecipeIsGrindingRecipe = true;
            correctIncrementAmount = itemStack2.getCount();
        }
        else {
            craftRecipeIsGrindingRecipe = false;
        }
    }

    @ModifyArg(method = "craftRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;increment(I)V"))
    private static int craftRecipeModifyIncrementAmount(int amount) {
        if (craftRecipeIsGrindingRecipe && amount != correctIncrementAmount) {
            return correctIncrementAmount;
        }
        else {
            return amount;
        }
    }

    @Inject(method = "canAcceptRecipeOutput", at = @At("RETURN"), cancellable = true)
    private static void injectCanAcceptRecipeOutput(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots,
                                                    int count, CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue() && recipe instanceof GrindingRecipe) {
            ItemStack grinderOutput = slots.get(2);
            ItemStack recipeOutput = recipe.getOutput();
            info.setReturnValue((grinderOutput.getCount() + recipeOutput.getCount() <= count &&
                    grinderOutput.getCount() + recipeOutput.getCount() <= grinderOutput.getMaxCount()) ||
                    grinderOutput.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxCount());
        }
    }
}
