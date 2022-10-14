package net.nightfallclosure.stonegrinder.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;
import net.nightfallclosure.stonegrinder.mixin.AbstractFurnaceScreenAccessor;

public class GrinderScreen extends AbstractFurnaceScreen<GrinderScreenHandler> {
    public static final Identifier GRINDER_GUI_TEXTURE = new Identifier(StoneGrinderMod.MOD_ID,
            "textures/gui/grinder_gui.png");

    public GrinderScreen(GrinderScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new EmptyRecipeBookScreen(), inventory, title, GRINDER_GUI_TEXTURE);
    }

    @Override
    public void init() {
        super.init();

        // This call is to hide the recipe book icon
        // It might be better to use a mixin
        this.clearChildren();

        if (this.recipeBook.isOpen()) {
            this.recipeBook.toggleOpen();
            this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
        }
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
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
