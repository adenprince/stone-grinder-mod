package net.nightfallclosure.stonegrinder.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.nightfallclosure.stonegrinder.StoneGrinderMod;

public class ModStats {
    private static final String INTERACT_WITH_GRINDER_STAT_NAME = "interact_with_grinder";

    public static final Identifier INTERACT_WITH_GRINDER = new Identifier(StoneGrinderMod.MOD_ID,
            INTERACT_WITH_GRINDER_STAT_NAME);

    public static void registerStats() {
        Registry.register(Registries.CUSTOM_STAT, INTERACT_WITH_GRINDER_STAT_NAME, INTERACT_WITH_GRINDER);
        Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_GRINDER, StatFormatter.DEFAULT);
    }
}
