package net.nightfallclosure.stonegrinder.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.nightfallclosure.stonegrinder.StoneGrinder;
import net.nightfallclosure.stonegrinder.block.ModBlocks;
import net.nightfallclosure.stonegrinder.recipe.GrindingRecipe;

public class GrindingRecipeCategory implements IRecipeCategory<GrindingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(StoneGrinder.MOD_ID, "grinding");
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(StoneGrinder.MOD_ID, "textures/gui/grinder_gui.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated animatedFlame;
    private final LoadingCache<Integer, IDrawableAnimated> arrowCache;

    private static final int defaultCookingTime = 100;

    public GrindingRecipeCategory(IGuiHelper guiHelper) {
        IDrawableStatic staticFlame = guiHelper.createDrawable(TEXTURE, 176, 0, 14, 14);

        this.background = guiHelper.createDrawable(TEXTURE, 37, 16, 100, 54);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.GRINDER_BLOCK.get()));
        this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300,
                IDrawableAnimated.StartDirection.TOP, true);
        this.arrowCache = CacheBuilder.newBuilder().maximumSize(25).build(new CacheLoader<>() {
            @Override
            public IDrawableAnimated load(Integer cookingTime) {
                return guiHelper.drawableBuilder(TEXTURE, 176, 14, 24, 17)
                        .buildAnimated(cookingTime, IDrawableAnimated.StartDirection.LEFT, false);
            }
        });
    }

    @Override
    public RecipeType<GrindingRecipe> getRecipeType() {
        return StoneGrinderPlugin.GRINDING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.stonegrinder.grinding");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(GrindingRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     PoseStack poseStack, double mouseX, double mouseY) {
        animatedFlame.draw(poseStack, 1, 2);
        getArrow(recipe).draw(poseStack, 42, 18);
        drawExperience(recipe, poseStack, 0);
        drawCookingTime(recipe, poseStack, 45);
    }

    private IDrawableAnimated getArrow(GrindingRecipe recipe) {
        int cookingTime = recipe.getCookingTime();
        if (cookingTime <= 0) {
            cookingTime = defaultCookingTime;
        }
        return this.arrowCache.getUnchecked(cookingTime);
    }

    private void drawExperience(GrindingRecipe recipe, PoseStack poseStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable(
                    "category.stonegrinder.grinding.experience", experience);
            Font fontRenderer = Minecraft.getInstance().font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(poseStack, experienceString, background.getWidth() - stringWidth,
                    y, 0xFF808080);
        }
    }

    private void drawCookingTime(GrindingRecipe recipe, PoseStack poseStack, int y) {
        int cookingTime = recipe.getCookingTime();
        if (cookingTime > 0) {
            int cookingTimeSeconds = cookingTime / 20;
            Component cookingTimeSecondsString = Component.translatable(
                    "category.stonegrinder.grinding.time.seconds", cookingTimeSeconds);
            Font fontRenderer = Minecraft.getInstance().font;
            int stringWidth = fontRenderer.width(cookingTimeSecondsString);
            fontRenderer.draw(poseStack, cookingTimeSecondsString, background.getWidth() - stringWidth,
                    y, 0xFF808080);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GrindingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 37).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 79, 19).addItemStack(recipe.getResultItem());
    }
}
