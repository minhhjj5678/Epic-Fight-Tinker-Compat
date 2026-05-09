package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.*;
import java.util.function.BiFunction;

public record WeaponSet(
        List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> attackMotions,
        List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> mountAttackMotions,
        Map<LivingMotion, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>> livingMotions,
        Map<GuardSkill.BlockType, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>> guardMotions,
        BiFunction<ItemStack, PlayerPatch<?>, Skill> innateSkill,
        Skill passiveSkill,
        BiFunction<PlayerPatch<?>, InteractionHand, LivingMotion> motionPredicate,
        Boolean visibleOffhand
) {

    public WeaponSet(WeaponSetBuilder builder) {
        this(builder.attackMotions, builder.mountAttackMotions, builder.livingMotions, builder.guardMotions, builder.innateSkill, builder.passiveSkill, builder.motionPredicate, builder.visibleOffhand);
    }

    public static WeaponSetBuilder builder() {
        return new WeaponSetBuilder();
    }

    public static class WeaponSetBuilder {
        List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> attackMotions;
        List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> mountAttackMotions;
        Map<LivingMotion, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>> livingMotions;
        Map<GuardSkill.BlockType, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>> guardMotions;
        BiFunction<ItemStack, PlayerPatch<?>, Skill> innateSkill;
        Skill passiveSkill;
        BiFunction<PlayerPatch<?>, InteractionHand, LivingMotion> motionPredicate;
        Boolean visibleOffhand;

        private WeaponSetBuilder() {
            attackMotions = new ArrayList<>();
            mountAttackMotions = new ArrayList<>();
            livingMotions = new HashMap<>();
            guardMotions = new HashMap<>();
            innateSkill = (item, patch) -> null;
            motionPredicate = (patch, hand) -> null;
            visibleOffhand = false;
        }

        @SafeVarargs
        public final WeaponSetBuilder addComboAttacks(AnimationManager.AnimationAccessor<? extends AttackAnimation>... attackMotions) {
            this.attackMotions.addAll(List.of(attackMotions));
            return this;
        }

        @SafeVarargs
        public final WeaponSetBuilder addMountAttacks(AnimationManager.AnimationAccessor<? extends AttackAnimation>... mountAttackMotions) {
            this.mountAttackMotions.addAll(List.of(mountAttackMotions));
            return this;
        }

        public WeaponSetBuilder addInnateSkill(BiFunction<ItemStack, PlayerPatch<?>, Skill> innateSkill) {
            this.innateSkill = innateSkill;
            return this;
        }

        @SafeVarargs
        public final WeaponSetBuilder addGuardAnimations(GuardSkill.BlockType type, AnimationManager.AnimationAccessor<? extends StaticAnimation>... animations) {
            this.guardMotions.computeIfAbsent(type, (K) -> new ArrayList<>()).addAll(List.of(animations));
            return this;
        }

        public WeaponSetBuilder addLivingMotionModifier(LivingMotion livingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation> animation) {
            this.livingMotions.computeIfAbsent(livingMotion, (K) -> new ArrayList<>()).add(animation);
            return this;
        }

        public WeaponSetBuilder addLivingMotionsRecursive(AnimationManager.AnimationAccessor<? extends StaticAnimation> animation, LivingMotion... livingMotions) {
            for(LivingMotion livingMotion : livingMotions) {
                this.addLivingMotionModifier(livingMotion, animation);
            }
            return this;
        }

        public WeaponSetBuilder setPassiveSkill(Skill passiveSkill) {
            this.passiveSkill = passiveSkill;
            return this;
        }

        public WeaponSetBuilder setMotionPredicate(BiFunction<PlayerPatch<?>, InteractionHand, LivingMotion> motionPredicate) {
            this.motionPredicate = motionPredicate;
            return this;
        }

        public WeaponSetBuilder canBeVisibleOffhand() {
            this.visibleOffhand = true;
            return this;
        }

        public WeaponSet build() {
            return new WeaponSet(this);
        }
    }
}
