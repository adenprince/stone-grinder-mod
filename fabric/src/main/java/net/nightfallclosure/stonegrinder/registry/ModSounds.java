package net.nightfallclosure.stonegrinder.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;

public class ModSounds {
    private static final Identifier GRINDER_GRIND_SOUND_ID = new Identifier(StoneGrinderMod.MOD_ID, "grind");

    public static final SoundEvent GRINDER_GRIND_SOUND_EVENT = SoundEvent.of(GRINDER_GRIND_SOUND_ID);

    public static void registerSoundEvents() {
        Registry.register(Registries.SOUND_EVENT, GRINDER_GRIND_SOUND_ID, GRINDER_GRIND_SOUND_EVENT);
    }
}
