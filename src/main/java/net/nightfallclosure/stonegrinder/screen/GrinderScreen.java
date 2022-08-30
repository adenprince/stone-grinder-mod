package net.nightfallclosure.stonegrinder.screen;

import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.BlastFurnaceRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GrinderScreen extends AbstractFurnaceScreen<GrinderScreenHandler> {
    public static final Identifier TEXTURE = new Identifier("textures/gui/container/blast_furnace.png");

    public GrinderScreen(GrinderScreenHandler handler, PlayerInventory inventory, Text title) {
        // TODO: Fix using blast furnace recipe book screen
        super(handler, new BlastFurnaceRecipeBookScreen(), inventory, title, TEXTURE);
    }
}
