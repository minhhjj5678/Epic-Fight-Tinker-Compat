package com.minhhjjj.epicfighttinkercompat.gameasset.profiles;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

public record ModifierProfile(
        ModifierId modifierId,
        Function<LivingEntityPatch<?>, Style> styleProvider,
        Map<Style, CombatProfile> weaponSets,
        Collider collider,
        WeaponCategory weaponCategory,
        BiFunction<ToolStack, PlayerPatch<?>, Skill> innateSkill,
        int priority
) {

    public record CombatSet(
    ) {}

    public static Builder builder(ModifierId modifierId) {
        return new Builder(modifierId);
    }

    public static class Builder {
        private final ModifierId modifierId;
        private Function<LivingEntityPatch<?>, Style> styleProvider;
        private final Map<Style, CombatProfile> weaponSets;
        private Collider collider;
        private WeaponCategory weaponCategory;
        private int priority = 0;
        private BiFunction<ToolStack, PlayerPatch<?>, Skill> innateSkill = (item, patch) -> null;

        public Builder(ModifierId modifierId) {
            this.modifierId = modifierId;
            this.weaponSets = new HashMap<>();
        }

        public Builder styleProvider(Function<LivingEntityPatch<?>, Style> styleProvider) {
            this.styleProvider = styleProvider;
            return this;
        }

        public Builder addMoveSet(Style style, CombatProfile weaponSet) {
            this.weaponSets.put(style, weaponSet);
            return this;
        }

        public Builder collider(Collider collider) {
            this.collider = collider;
            return this;
        }

        public Builder weaponCategory(WeaponCategory weaponCategory) {
            this.weaponCategory = weaponCategory;
            return this;
        }

        public Builder innateSkill(BiFunction<ToolStack, PlayerPatch<?>, Skill> innateSkill) {
            this.innateSkill = innateSkill;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public final ModifierProfile build() {
            return new ModifierProfile(modifierId, styleProvider, weaponSets, collider, weaponCategory, innateSkill, priority);
        }
    }
}
