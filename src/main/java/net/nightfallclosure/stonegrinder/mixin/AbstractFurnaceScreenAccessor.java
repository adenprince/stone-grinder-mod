package net.nightfallclosure.stonegrinder.mixin;

import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceScreen.class)
public interface AbstractFurnaceScreenAccessor {
    @Accessor
    Identifier getBackground();
}
