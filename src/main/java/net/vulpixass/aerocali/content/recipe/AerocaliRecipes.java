package net.vulpixass.aerocali.content.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.content.recipe.recipes.NavCompassRecipe;

import java.util.function.Supplier;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public class AerocaliRecipes{
    // Registers/Defines the Custom Code written recipes
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MOD_ID);
    public static final Supplier<RecipeSerializer<NavCompassRecipe>> NAV_COMPASS_SERIALIZER = SERIALIZERS.register("nav_compass_recipe",
            () -> new SimpleCraftingRecipeSerializer<>(NavCompassRecipe::new));

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
