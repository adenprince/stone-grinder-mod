package net.nightfallclosure.stonegrinder.stat;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.nightfallclosure.stonegrinder.StoneGrinder;

public class ModStats {
    private static final String INTERACT_WITH_GRINDER_STAT_NAME = "interact_with_grinder";

    public static final ResourceLocation INTERACT_WITH_GRINDER =
            new ResourceLocation(StoneGrinder.MOD_ID, INTERACT_WITH_GRINDER_STAT_NAME);

    public static void registerStats() {
        Registry.register(Registry.CUSTOM_STAT, INTERACT_WITH_GRINDER_STAT_NAME, INTERACT_WITH_GRINDER);
        Stats.CUSTOM.get(INTERACT_WITH_GRINDER, StatFormatter.DEFAULT);
    }
}
