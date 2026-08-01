package com.minhhjjj.epicfighttinkercompat.gameasset.profiles;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import javax.annotation.Nonnull;

public record ModifierProfile(
        @Nonnull ModifierId modifierId,
        ResourceLocation weaponType,
        Function<ToolStack, Collider> collider,
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
        private ResourceLocation weaponType = null;
        private Function<ToolStack, Collider> collider = tool -> null;
        private WeaponCategory weaponCategory = null;
        private int priority = 0;
        private BiFunction<ToolStack, PlayerPatch<?>, Skill> innateSkill = (item, patch) -> null;

        public Builder(ModifierId modifierId) {
            this.modifierId = modifierId;
        }

        public Builder type(ResourceLocation type) {
            this.weaponType = type;
            return this;
        }

        public Builder collider(Function<ToolStack, Collider> collider) {
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
            return new ModifierProfile(modifierId, weaponType, collider, weaponCategory, innateSkill, priority);
        }
    }
}
