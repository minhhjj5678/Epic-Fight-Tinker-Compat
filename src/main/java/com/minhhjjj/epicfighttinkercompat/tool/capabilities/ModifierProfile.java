package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.Map;
import java.util.function.Function;

import slimeknights.tconstruct.library.modifiers.ModifierId;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.ex_cap.core.data.MoveSet;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

public class ModifierProfile {
    private final ModifierId modifierId;
    private final Function<LivingEntityPatch<?>, Style> styleProvider;
    private final Map<Style, MoveSet> moveSets;
    private final Collider collider;
    private final WeaponCategory weaponCategory;
    private final int priority;

    public ModifierProfile(ModifierId modifierId, Function<LivingEntityPatch<?>, Style> styleProvider, Map<Style, MoveSet> moveSets, WeaponCategory weaponCategory, Collider collider, int priority) {
        this.modifierId = modifierId;
        this.styleProvider = styleProvider;
        this.moveSets = moveSets;
        this.weaponCategory = weaponCategory;
        this.collider = collider;
        this.priority = priority;
    }

    public ModifierId getModifierId() {
        return modifierId;
    }

    public Function<LivingEntityPatch<?>, Style> getStyleProvider() {
        return styleProvider;
    }

    public Map<Style, MoveSet> getMoveSets() {
        return moveSets;
    }

    public int getPriority() {
        return priority;
    }

    public WeaponCategory getWeaponCategory() {
        return weaponCategory;
    }

    public Collider getCollider() {
        return collider;
    }
}
