package net.nightfallclosure.stonegrinder.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;

public class ModSounds {
    private static final Identifier GRINDER_GRIND_SOUND_ID = new Identifier(StoneGrinderMod.MOD_ID, "grind");

    public static final SoundEvent GRINDER_GRIND_SOUND_EVENT = new SoundEvent(GRINDER_GRIND_SOUND_ID);

    public static void registerSoundEvents() {
        Registry.register(Registry.SOUND_EVENT, GRINDER_GRIND_SOUND_ID, GRINDER_GRIND_SOUND_EVENT);
    }
}
