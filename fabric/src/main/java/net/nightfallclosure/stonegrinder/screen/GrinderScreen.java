package net.nightfallclosure.stonegrinder.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;
import net.nightfallclosure.stonegrinder.mixin.AbstractFurnaceScreenAccessor;

public class GrinderScreen extends AbstractFurnaceScreen<GrinderScreenHandler> {
    public static final Identifier GRINDER_GUI_TEXTURE = new Identifier(StoneGrinderMod.MOD_ID,
            "textures/gui/grinder_gui.png");

    private final boolean blastFurnaceRecipeBookWasOpen;

    public GrinderScreen(GrinderScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new EmptyRecipeBookScreen(), inventory, title, GRINDER_GUI_TEXTURE);

        blastFurnaceRecipeBookWasOpen = MinecraftClient.getInstance().player != null &&
                MinecraftClient.getInstance().player.getRecipeBook().isGuiOpen(RecipeBookCategory.BLAST_FURNACE);
    }

    @Override
    public void init() {
        super.init();

        // Hide the recipe book icon
        this.clearChildren();

        if (this.recipeBook.isOpen()) {
            this.recipeBook.toggleOpen();
            this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);

            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.getRecipeBook().setGuiOpen(RecipeBookCategory.BLAST_FURNACE,
                        false);
            }
        }
    }

    @Override
    public void removed() {
        if (MinecraftClient.getInstance().player != null && blastFurnaceRecipeBookWasOpen) {
            this.recipeBook.toggleOpen();
            MinecraftClient.getInstance().player.getRecipeBook().setGuiOpen(RecipeBookCategory.BLAST_FURNACE,
                    true);
        }
        super.removed();
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ((AbstractFurnaceScreenAccessor)this).getBackground());

        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        if (this.handler.isBurning()) {
            int fuelProgress = this.handler.getFuelProgress();
            this.drawTexture(matrices, x + 38, y + 30 - fuelProgress,
                    176, 12 - fuelProgress, 14, fuelProgress + 1);
        }

        int cookProgress = this.handler.getCookProgress();
        this.drawTexture(matrices, x + 79, y + 34, 176, 14, cookProgress + 1, 16);
    }
}
