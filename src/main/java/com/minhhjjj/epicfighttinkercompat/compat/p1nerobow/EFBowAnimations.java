/*
 * This file is derived from P1nero Epic Bow, originally authored by P1nero.
 * Source: https://github.com/P1neapplell0/Epic-Bow/blob/1.20.1/src/main/java/com/p1nero/epicfightbow/gameassets/EFBowAnimations.java
 * This code is licensed under the GNU LGPL.
 *
 * Modifications made by: minhhjjj
 */
package com.minhhjjj.epicfighttinkercompat.compat.p1nerobow;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.StunType;

import java.util.function.Predicate;

public class EFBowAnimations {
    private static final Collider BOW_DASH = new MultiOBBCollider(2, 1, 1.5, 1, 0, 0, 0);
    private static final Collider BOW_ELBOW = new MultiOBBCollider(2, 1, 1, 1, 0, 1, 0);
    private static final Collider BOW_SCAN = new MultiOBBCollider(2, 8, 4D, 16, 0.0D, 1, -16);

    public static AnimationManager.AnimationAccessor<MovementAnimation> BOW_RUN;
    public static AnimationManager.AnimationAccessor<TCScanAttackAnimation> BOW_AUTO1;
    public static AnimationManager.AnimationAccessor<TCScanAttackAnimation> BOW_AUTO2;
    public static AnimationManager.AnimationAccessor<TCScanAttackAnimation> BOW_AUTO3;
    public static AnimationManager.AnimationAccessor<AttackAnimation> BOW_DASH_ATTACK;
    public static AnimationManager.AnimationAccessor<TCScanAttackAnimation> BOW_JUMP_ATTACK;
    public static AnimationManager.AnimationAccessor<AttackAnimation> ELBOW_2;
    public static AnimationManager.AnimationAccessor<AttackAnimation> ELBOW_3;

    private static final String MOD_ID = "p1nero_bow";

    @SuppressWarnings("unchecked")
    public static AnimationManager.AnimationAccessor<? extends AttackAnimation>[] getComboAttack() {
        return ModList.get().isLoaded(MOD_ID) ? new AnimationManager.AnimationAccessor[]{
                BOW_AUTO1, BOW_AUTO2, BOW_AUTO3, BOW_DASH_ATTACK, BOW_JUMP_ATTACK
        }
                : new AnimationManager.AnimationAccessor[]{
                Animations.FIST_AUTO1, Animations.FIST_AUTO2, Animations.FIST_AUTO3,
                Animations.FIST_DASH, Animations.FIST_AIR_SLASH
        };
    }

    public static void buildBowAnimations(AnimationManager.AnimationBuilder builder) {
        if (!ModList.get().isLoaded(MOD_ID)) return;
        BOW_RUN = builder.nextAccessor("biped/bow_run", accessor -> new MovementAnimation(true, accessor, Armatures.BIPED)
                .setResourceLocation(MOD_ID, "biped/bow_run")
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, ((dynamicAnimation, livingEntityPatch, v, v1, v2) -> BOW_RUN.get().getPlaySpeed(livingEntityPatch, dynamicAnimation) * 2.0F)));
        BOW_AUTO1 = builder.nextAccessor("biped/bow_auto1", accessor ->
                new TCScanAttackAnimation(0.15F, 0, 0.15F, 65 / 60F, 65 / 60F,
                        InteractionHand.MAIN_HAND, BOW_SCAN, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .setResourceLocation(MOD_ID, "biped/bow_auto1")
                        .addProperty(AnimationProperty.AttackAnimationProperty.PLAY_SPEED_MODIFIER,
                                (dynamicAnimation, living, defaultSpeed, v1, v2) -> {
                                    ItemStack itemStack = living.getOriginal().getMainHandItem();
                                    if (itemStack.getItem() instanceof ModifiableBowItem) {
                                        ToolStack tool = ToolStack.from(itemStack);
                                        return tool.getStats().get(ToolStats.DRAW_SPEED);
                                    }
                                    return 1.0F;
                                })
                        .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                        .addEvents(setFullBowUseTime(20 / 60F), shootIn(30 / 60F),
                                setFullBowUseTime(50 / 60F), shootIn(60 / 60F)));
        BOW_AUTO2 = builder.nextAccessor("biped/bow_auto2", accessor ->
                new TCScanAttackAnimation(0.15F, 0, 0.15F, 65 / 60F, 65 / 60F,
                        InteractionHand.MAIN_HAND, BOW_SCAN, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .setResourceLocation(MOD_ID, "biped/bow_auto2")
                        .addProperty(AnimationProperty.AttackAnimationProperty.PLAY_SPEED_MODIFIER,
                                (dynamicAnimation, living, defaultSpeed, v1, v2) -> {
                                    ItemStack itemStack = living.getOriginal().getMainHandItem();
                                    if (itemStack.getItem() instanceof ModifiableBowItem) {
                                        ToolStack tool = ToolStack.from(itemStack);
                                        return tool.getStats().get(ToolStats.DRAW_SPEED);
                                    }
                                    return 1.0F;
                                })
                        .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                        .addEvents(setFullBowUseTime(20 / 60F), shootIn(30 / 60F),
                                setFullBowUseTime(50 / 60F), shootIn(60 / 60F)));
        BOW_AUTO3 = builder.nextAccessor("biped/bow_auto3", accessor ->
                new TCScanAttackAnimation(0.15F, 0, 0.15F, 100 / 60F, 120 / 60F,
                        InteractionHand.MAIN_HAND, BOW_SCAN, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .setResourceLocation(MOD_ID, "biped/bow_auto3")
                        .addProperty(AnimationProperty.AttackAnimationProperty.PLAY_SPEED_MODIFIER,
                                (dynamicAnimation, living, defaultSpeed, v1, v2) -> {
                            ItemStack itemStack = living.getOriginal().getMainHandItem();
                            if (itemStack.getItem() instanceof ModifiableBowItem) {
                                ToolStack tool = ToolStack.from(itemStack);
                                return tool.getStats().get(ToolStats.DRAW_SPEED);
                            }
                            return 1.0F;
                                })
                        .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                        .addEvents(setFullBowUseTime(50 / 60F), shootIn(80/60F),
                                setFullBowUseTime(85 / 60F), shootIn(90/60F),
                                setFullBowUseTime(95 / 60F), shootIn(100/60F)));
        BOW_DASH_ATTACK = builder.nextAccessor("biped/bow_dash_attack", accessor ->
                new AttackAnimation(0.15F, 0, 0, 40 / 60F, 60 / 60F,
                        BOW_DASH, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0f))
                        .setResourceLocation(MOD_ID, "biped/bow_dash_attack")
                        .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true));
        BOW_JUMP_ATTACK = builder.nextAccessor("biped/bow_jump_attack", accessor ->
                new TCScanAttackAnimation(0.15F, 0, 0.15F, 20 / 60F, 80 / 60F,
                        InteractionHand.MAIN_HAND, BOW_SCAN, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .setResourceLocation(MOD_ID, "biped/bow_jump_attack")
                        .addEvents(setFullBowUseTime(10 / 60F), shootIn(15 / 60F),
                                setFullBowUseTime(16 / 60F), shootIn(20 / 60F)));

        ELBOW_2 = builder.nextAccessor("biped/elbow_2", accessor ->
                new AttackAnimation(0.15F, 20 / 60F, 20 / 60F, 40 / 60F, 50 / 60F,
                        BOW_ELBOW, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.FALL)
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.2F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(8.0F))
                        .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true));

        ELBOW_3 = builder.nextAccessor("biped/elbow_3", accessor ->
                new AttackAnimation(0.15F, 20 / 60F, 20 / 60F, 40 / 60F, 50 / 60F,
                        BOW_ELBOW, Armatures.BIPED.get().handR, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(8.0F)));

    }

    public static AnimationEvent.InTimeEvent<?> setFullBowUseTime(float time) {
        return AnimationEvent.InTimeEvent.create(time, (livingEntityPatch, assetAccessor, animationParameters) -> livingEntityPatch.getOriginal().getMainHandItem().getOrCreateTag().putBoolean("is_full", true), AnimationEvent.Side.CLIENT);
    }
    
    public static AnimationEvent.InTimeEvent<?> shootIn(float time) {
        return AnimationEvent.InTimeEvent.create(time, ((livingEntityPatch, assetAccessor, animationParameters) -> {
            // no double skill for now
            shootOnce(livingEntityPatch, 3.0F);
            livingEntityPatch.getOriginal().stopUsingItem();
        }), AnimationEvent.Side.BOTH);
    }

    private static void shootOnce(LivingEntityPatch<?> livingEntityPatch) {
        shootOnce(livingEntityPatch, 3.0F);
    }
    private static void shootOnce(LivingEntityPatch<?> livingEntityPatch, float speed) {
        LivingEntity living = livingEntityPatch.getOriginal();
        ItemStack itemStack = living.getMainHandItem();
        itemStack.getOrCreateTag().putBoolean("is_full", false);
        ToolStack toolStack = ToolStack.from(itemStack);
        Item item = itemStack.getItem();
        Level level = living.level();
        int leftTime = 0;
        if (livingEntityPatch.getOriginal() instanceof ServerPlayer player && item instanceof ModifiableBowItem bowItem) {
            boolean flag = player.getAbilities().instabuild || itemStack.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0;

            Predicate<ItemStack> ammoPredicate;
            switch (toolStack.getPersistentData().getInt(ModifiableBowItem.KEY_BALLISTA)) {
                case 1 -> ammoPredicate = null;
                case 2 -> ammoPredicate = (stack) -> stack.is(TinkerTags.Items.BALLISTA_AMMO);
                case 3 -> ammoPredicate = bowItem.getSupportedHeldProjectiles();
                default -> ammoPredicate = ModifiableBowItem.isBallista(toolStack) ? bowItem.getSupportedBallistaAmmo() : bowItem.getSupportedHeldProjectiles();
            }
            ItemStack ammo = BowAmmoModifierHook.getAmmo(toolStack, itemStack, player, ammoPredicate);

            int i = item.getUseDuration(itemStack) - leftTime;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(itemStack, level, player, i, !ammo.isEmpty() || flag);
            if (i < 0) return;

            if (!ammo.isEmpty() || flag) {
                if (ammo.isEmpty()) {
                    ammo = new ItemStack(Items.ARROW);
                }

                float f = 1.0F;
                boolean flag1 = player.getAbilities().instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem) ammo.getItem()).isInfinite(ammo, itemStack, player));
                if (!level.isClientSide) {
                    ArrowItem arrowitem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
                    AbstractArrow abstractarrow = arrowitem.createArrow(level, ammo, player);
//                    abstractarrow = bowItem.customArrow(abstractarrow);
                    abstractarrow.setPos(getJointWorldPos(livingEntityPatch, Armatures.BIPED.get().handL));
                    LivingEntity target = TCScanAttackAnimation.getTarget(livingEntityPatch);
                    if(target == null) {
                        abstractarrow.shootFromRotation(player, living.getXRot(), livingEntityPatch.getYRot(), 0.0F, f * 3.0F, 1.0F);
                    } else {
                        Vec3 targetPos = target.getEyePosition();
                        Vec3 vec3 = targetPos.subtract(abstractarrow.position()).normalize().scale(speed * f);
                        abstractarrow.setDeltaMovement(vec3);
                        double d0 = vec3.horizontalDistance();
                        abstractarrow.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
                        abstractarrow.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
                        abstractarrow.yRotO = abstractarrow.getYRot();
                        abstractarrow.xRotO = abstractarrow.getXRot();
                    }
                    if (f == 1.0F) {
                        abstractarrow.setCritArrow(true);
                    }

                    int j = itemStack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
                    if (j > 0) {
                        abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                    }

                    int k = itemStack.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
                    if (k > 0) {
                        abstractarrow.setKnockback(k);
                    }

                    if (itemStack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
                        abstractarrow.setSecondsOnFire(100);
                    }

                    itemStack.hurtAndBreak(1, player, (p_289501_) -> p_289501_.broadcastBreakEvent(player.getUsedItemHand()));
                    if (flag1 || player.getAbilities().instabuild && (ammo.is(Items.SPECTRAL_ARROW) || ammo.is(Items.TIPPED_ARROW))) {
                        abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    level.addFreshEntity(abstractarrow);
                }

                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                if (!flag1 && !player.getAbilities().instabuild) {
                    ammo.shrink(1);
                    if (ammo.isEmpty()) {
                        player.getInventory().removeItem(ammo);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(bowItem));
            }
        }
    }

    public static Vec3 getJointWorldPos(LivingEntityPatch<?> entityPatch, Joint joint) {
        LivingEntity entity = entityPatch.getOriginal();
        OpenMatrix4f transformMatrix = entityPatch.getArmature().getBoundTransformFor(entityPatch.getAnimator().getPose(0.1f), joint);
        OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entity.yBodyRotO + 180.0F), new Vec3f(0.0F, 1.0F, 0.0F));
        OpenMatrix4f.mul(rotation, transformMatrix, transformMatrix);
        return new Vec3(transformMatrix.m30 + (float) entity.getX(), transformMatrix.m31 + (float) entity.getY(), transformMatrix.m32 + (float) entity.getZ());
    }

}
