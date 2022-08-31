package net.nightfallclosure.stonegrinder.screen;

import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.BlastFurnaceRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GrinderScreen extends AbstractFurnaceScreen<GrinderScreenHandler> {
    public static final Identifier TEXTURE = new Identifier("textures/gui/container/blast_furnace.png");

    public GrinderScreen(GrinderScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new BlastFurnaceRecipeBookScreen(), inventory, title, TEXTURE);
    }

    @Override
    public void init() {
        super.init();

        // This call is to hide the recipe book icon
        // It might be better to use a mixin
        this.clearChildren();
    }
}
