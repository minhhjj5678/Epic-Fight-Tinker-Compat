package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.world.InteractionHand;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class TCWeaponUtils {

    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    /**
     * A highly flexible method for retrieving dynamic properties.
     *
     * @param <T>             The dynamic return type (e.g., WeaponCategories, Collider, Float, etc.)
     * @param entityPatch     The PlayerPatch to pass if the weapon is from Tinkers' Construct
     * @param itemCap         The capability of the current weapon
     * @param hand            The InteractionHand holding the weapon
     * @param defaultMethod   The default method to call (without the PlayerPatch parameter)
     * @param tcMethod        The specific method for TCWeaponCapability (with the PlayerPatch parameter)
     * @return                The dynamic property value of type <T>
     */
    public static <T> T getDynamicProperty(
            LivingEntityPatch<?> entityPatch,
            CapabilityItem itemCap,
            InteractionHand hand,
            Function<CapabilityItem, T> defaultMethod,
            TriFunction<TCWeaponCapability, LivingEntityPatch<?>, InteractionHand, T> tcMethod
    ) {
        if (itemCap instanceof TCWeaponCapability tcCap) {
            return tcMethod.apply(tcCap, entityPatch, hand);
        }

        return defaultMethod.apply(itemCap);
    }

    private TCWeaponUtils() {}
}