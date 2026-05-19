package com.minhhjjj.epicfighttinkercompat.gameasset.profiles;

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

public record CombatProfile(
        List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> attackMotions,
        List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> mountAttackMotions,
        Map<LivingMotion, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>> livingMotions,
        Map<GuardSkill.BlockType, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>> guardMotions,
        BiFunction<ItemStack, PlayerPatch<?>, Skill> innateSkill,
        Skill passiveSkill,
        BiFunction<PlayerPatch<?>, InteractionHand, LivingMotion> motionPredicate,
        Boolean visibleOffhand
) {

    public CombatProfile(CombatProfileBuilder builder) {
        this(builder.attackMotions, builder.mountAttackMotions, builder.livingMotions, builder.guardMotions, builder.innateSkill, builder.passiveSkill, builder.motionPredicate, builder.visibleOffhand);
    }

    public static CombatProfileBuilder builder() {
        return new CombatProfileBuilder();
    }

    public static class CombatProfileBuilder {
        List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> attackMotions;
        List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> mountAttackMotions;
        Map<LivingMotion, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>> livingMotions;
        Map<GuardSkill.BlockType, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>> guardMotions;
        BiFunction<ItemStack, PlayerPatch<?>, Skill> innateSkill;
        Skill passiveSkill;
        BiFunction<PlayerPatch<?>, InteractionHand, LivingMotion> motionPredicate;
        boolean visibleOffhand;

        private CombatProfileBuilder() {
            attackMotions = new ArrayList<>();
            mountAttackMotions = new ArrayList<>();
            livingMotions = new HashMap<>();
            guardMotions = new HashMap<>();
            innateSkill = (item, patch) -> null;
            motionPredicate = (patch, hand) -> null;
            visibleOffhand = false;
        }

        @SafeVarargs
        public final CombatProfileBuilder addComboAttacks(AnimationManager.AnimationAccessor<? extends AttackAnimation>... attackMotions) {
            this.attackMotions.addAll(List.of(attackMotions));
            return this;
        }

        @SafeVarargs
        public final CombatProfileBuilder addMountAttacks(AnimationManager.AnimationAccessor<? extends AttackAnimation>... mountAttackMotions) {
            this.mountAttackMotions.addAll(List.of(mountAttackMotions));
            return this;
        }

        public CombatProfileBuilder addInnateSkill(BiFunction<ItemStack, PlayerPatch<?>, Skill> innateSkill) {
            this.innateSkill = innateSkill;
            return this;
        }

        @SafeVarargs
        public final CombatProfileBuilder addGuardAnimations(GuardSkill.BlockType type, AnimationManager.AnimationAccessor<? extends StaticAnimation>... animations) {
            this.guardMotions.computeIfAbsent(type, (K) -> new ArrayList<>()).addAll(List.of(animations));
            return this;
        }

        public CombatProfileBuilder addLivingMotionModifier(LivingMotion livingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation> animation) {
            this.livingMotions.computeIfAbsent(livingMotion, (K) -> new ArrayList<>()).add(animation);
            return this;
        }

        public CombatProfileBuilder addLivingMotionsRecursive(AnimationManager.AnimationAccessor<? extends StaticAnimation> animation, LivingMotion... livingMotions) {
            for(LivingMotion livingMotion : livingMotions) {
                this.addLivingMotionModifier(livingMotion, animation);
            }
            return this;
        }

        public CombatProfileBuilder setPassiveSkill(Skill passiveSkill) {
            this.passiveSkill = passiveSkill;
            return this;
        }

        public CombatProfileBuilder setMotionPredicate(BiFunction<PlayerPatch<?>, InteractionHand, LivingMotion> motionPredicate) {
            this.motionPredicate = motionPredicate;
            return this;
        }

        public CombatProfileBuilder canBeVisibleOffhand() {
            this.visibleOffhand = true;
            return this;
        }

        public CombatProfile build() {
            return new CombatProfile(this);
        }
    }
}
