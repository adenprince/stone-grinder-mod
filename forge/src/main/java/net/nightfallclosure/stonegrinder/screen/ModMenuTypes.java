package net.nightfallclosure.stonegrinder.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nightfallclosure.stonegrinder.StoneGrinder;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, StoneGrinder.MOD_ID);

    public static final RegistryObject<MenuType<GrinderMenu>> GRINDER_MENU_TYPE =
            registerMenuType(GrinderMenu::new, "grinder_menu_type");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(
            MenuType.MenuSupplier<T> factory, String name) {
        return MENU_TYPES.register(name, () -> new MenuType<>(factory));
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
