package net.nightfallclosure.stonegrinder.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityAccessor {
    @Accessor
    RecipeManager.MatchGetter<Inventory, ? extends AbstractCookingRecipe> getMatchGetter();

    @Accessor
    int getCookTime();
}
