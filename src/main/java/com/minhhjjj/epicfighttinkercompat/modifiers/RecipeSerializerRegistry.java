package com.minhhjjj.epicfighttinkercompat.modifiers;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;

public class RecipeSerializerRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "epicfighttinkercompat");

    public static final RegistryObject<RecipeSerializer<?>> EXTRACTION_SERIALIZER = SERIALIZERS.register("rare_weapon_extraction", () -> LoadableRecipeSerializer.of(RareWeaponExtractionRecipe.LOADER));

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}