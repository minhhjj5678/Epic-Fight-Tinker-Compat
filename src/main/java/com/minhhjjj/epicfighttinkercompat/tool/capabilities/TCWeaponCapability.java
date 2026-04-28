package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.ex_cap.core.data.MoveSet;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

public class TCWeaponCapability extends WeaponCapability {
    protected final ItemStack boundItem;
    protected MoveSet defaultMoveSet;
    protected List<ModifierProfile> modifierProfiles;
    private static final ModifierId THROWING_ID = new ModifierId(TConstruct.MOD_ID, "throwing");
    private static final ModifierId BLOCKING_ID = new ModifierId(TConstruct.MOD_ID, "blocking");
    private static final ThreadLocal<ModifierProfile> CONTEXT_PROFILE = new ThreadLocal<>();

    protected TCWeaponCapability(CapabilityItem.Builder builder) {
        super(builder);
        Builder tcBuilder = (Builder)builder;
        this.defaultMoveSet = tcBuilder.defaultMoveSet;
        this.modifierProfiles = tcBuilder.modifierProfiles;
        this.boundItem = tcBuilder.boundItem;
    }

    @Override
    public MoveSet getCurrentSet(LivingEntityPatch<?> patch) {
        ToolStack toolStack = getToolStack(patch);
        CONTEXT_PROFILE.remove();

        if (toolStack != null) {
            ModifierProfile profile = null;
            for (ModifierProfile modifierProfile : this.modifierProfiles) {
                ModifierId modifierId = modifierProfile.modifierId();
                if (modifierId == null) {
                    continue;
                }

                int level = toolStack.getModifierLevel(modifierId);
                if (level > 0 && (profile == null || modifierProfile.priority() > profile.priority())) {
                    profile = modifierProfile;
                }
            }

            if (profile != null) {
                Style resolvedStyle = profile.styleProvider().apply(patch);
                MoveSet moveSet = profile.moveSets().get(resolvedStyle);
                if (moveSet != null) {
                    CONTEXT_PROFILE.set(profile);
                    return moveSet;
                }
            }
        }

        MoveSet styleMoveSet = this.moveSets.get(this.getStyle(patch));
        if (styleMoveSet != null) {
            return styleMoveSet;
        }

        return this.defaultMoveSet != null ? this.defaultMoveSet : this.moveSets.get(Styles.COMMON);
    }

    @Override
    public WeaponCategory getWeaponCategory() {
        ModifierProfile profile = CONTEXT_PROFILE.get();
        if (profile != null && profile.weaponCategory() != null) {
            return profile.weaponCategory();
        }
        return super.getWeaponCategory();
    }

    @Override
    public Collider getWeaponCollider() {
        ModifierProfile profile = CONTEXT_PROFILE.get();
        if (profile != null && profile.collider() != null) {
            return profile.collider();
        }
        return super.getWeaponCollider();
    }

    @Override
    public LivingMotion getLivingMotion(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        InteractionHand checkedHand = Objects.requireNonNull(hand, "hand");
        if (entityPatch instanceof PlayerPatch<?> playerPatch && playerPatch.getOriginal().isCrouching() && playerPatch.getOriginal().isUsingItem() && playerPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.SPEAR) {
            ItemStack heldItem = playerPatch.getOriginal().getMainHandItem();
            ItemStack useItem = Objects.requireNonNull(playerPatch.getOriginal().getUseItem(), "useItem");
            if (!heldItem.isEmpty() && ItemStack.isSameItemSameTags(heldItem, useItem) && ToolStack.from(heldItem).getModifierLevel(Objects.requireNonNull(THROWING_ID, "throwing modifier id")) > 0) {
                return LivingMotions.AIM;
            }
        } 
        return super.getLivingMotion(entityPatch, checkedHand);
    }

    @Override
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getGuardMotion(GuardSkill skill, GuardSkill.BlockType blockType, PlayerPatch<?> playerpatch) {
        ToolStack toolStack = getToolStack(playerpatch);
        if (toolStack != null && toolStack.getModifierLevel(Objects.requireNonNull(BLOCKING_ID, "blocking modifier id")) > 0) {
            return super.getGuardMotion(skill, blockType, playerpatch);
        }
        return null;
    }

    @Override
    public UseAnim getUseAnimation(LivingEntityPatch<?> entityPatch) {
        MoveSet set = getCurrentSet(entityPatch);
        if (set != null && set.getLivingMotionModifiers().containsKey(LivingMotions.BLOCK) && getToolStack(entityPatch).getModifierLevel(Objects.requireNonNull(BLOCKING_ID, "blocking modifier id")) > 0) {
            return UseAnim.BLOCK;
        }
        return UseAnim.NONE;
    }

    protected ToolStack getToolStack(LivingEntityPatch<?> entityPatch) {
        ItemStack itemStack = entityPatch.getOriginal().getMainHandItem();
        return itemStack.isEmpty() ? null : ToolStack.from(itemStack);
    }

    protected ToolStack getToolStack() {
        return this.boundItem != null ? (this.boundItem.isEmpty() ? null : ToolStack.from(this.boundItem)) : null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends WeaponCapability.Builder {
        protected ItemStack boundItem;
        protected MoveSet defaultMoveSet;
        protected List<ModifierProfile> modifierProfiles;

        Builder() {
            super();
            this.constructor(TCWeaponCapability::new);
            this.modifierProfiles = new ArrayList<>();
        }

        public Builder defaultMoveSet(MoveSet.MoveSetBuilder moveSet) {
            this.defaultMoveSet = moveSet.build();
            return this;
        }

        public Builder addModifier(ModifierProfile modifierProfile) {
            if (modifierProfile != null && !this.modifierProfiles.contains(modifierProfile)) {
                this.modifierProfiles.add(modifierProfile);
            }
            return this;
        }

        public Builder boundItem(ItemStack itemStack) {
            this.boundItem = itemStack;
            return this;
        }
    }
}
