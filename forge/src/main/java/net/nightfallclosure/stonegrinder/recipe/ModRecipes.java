package net.nightfallclosure.stonegrinder.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nightfallclosure.stonegrinder.StoneGrinder;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, StoneGrinder.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, StoneGrinder.MOD_ID);

    public static final RegistryObject<RecipeType<GrindingRecipe>> GRINDING_RECIPE_TYPE =
            RECIPE_TYPES.register("grinding",
                    () -> RecipeType.simple(new ResourceLocation(StoneGrinder.MOD_ID, "grinding")));

    public static final RegistryObject<RecipeSerializer<GrindingRecipe>> GRINDING_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("grinding",
                    () -> new SimpleCookingSerializer<>(GrindingRecipe::new, 100));

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
