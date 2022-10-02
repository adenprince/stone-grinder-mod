package net.nightfallclosure.stonegrinder.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.BlastingRecipeBookComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.nightfallclosure.stonegrinder.StoneGrinder;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class GrinderScreen extends AbstractFurnaceScreen<GrinderMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(StoneGrinder.MOD_ID,
            "textures/gui/grinder_gui.png");

    public GrinderScreen(GrinderMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, new BlastingRecipeBookComponent(), pPlayerInventory, pTitle, TEXTURE);
    }

    @Override
    public void init() {
        super.init();

        // Hide the recipe book icon
        this.clearWidgets();
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int i = this.leftPos;
        int j = this.topPos;

        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            // TODO: Use constants in common folder for GUI element positions
            this.blit(pPoseStack, i + 38, j + 30 - litProgress,
                    176, 12 - litProgress, 14, litProgress + 1);
        }

        int burnProgress = this.menu.getBurnProgress();
        this.blit(pPoseStack, i + 79, j + 34, 176, 14, burnProgress + 1, 16);
    }
}
