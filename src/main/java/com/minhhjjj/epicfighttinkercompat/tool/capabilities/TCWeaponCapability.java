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
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.ex_cap.core.data.MoveSet;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

public class TCWeaponCapability extends WeaponCapability {
    protected MoveSet defaultMoveSet;
    protected List<ModifierProfile> modifierProfiles;
    private static final ModifierId THROWING_ID = new ModifierId(TConstruct.MOD_ID, "throwing");
    private static final ThreadLocal<ModifierProfile> CONTEXT_PROFILE = new ThreadLocal<>();

    protected TCWeaponCapability(CapabilityItem.Builder builder) {
        super(builder);
        Builder tcBuilder = (Builder)builder;
        this.defaultMoveSet = tcBuilder.defaultMoveSet;
        this.modifierProfiles = tcBuilder.modifierProfiles;
    }

    @Override
    public MoveSet getCurrentSet(LivingEntityPatch<?> patch) {
        ItemStack itemStack = patch.getOriginal().getMainHandItem();
        CONTEXT_PROFILE.remove();

        if (!itemStack.isEmpty()) {
            ToolStack toolStack = ToolStack.from(itemStack);

            ModifierProfile profile = null;
            for (ModifierProfile modifierProfile : this.modifierProfiles) {
                ModifierId modifierId = modifierProfile.getModifierId();
                if (modifierId == null) {
                    continue;
                }

                int level = toolStack.getModifierLevel(modifierId);
                if (level > 0 && (profile == null || modifierProfile.getPriority() > profile.getPriority())) {
                    profile = modifierProfile;
                }
            }

            if (profile != null) {
                Style resolvedStyle = profile.getStyleProvider().apply(patch);
                MoveSet moveSet = profile.getMoveSets().get(resolvedStyle);
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

        return this.defaultMoveSet != null ? this.defaultMoveSet : (MoveSet)this.moveSets.get(Styles.COMMON);
    }

    @Override
    public WeaponCategory getWeaponCategory() {
        ModifierProfile profile = CONTEXT_PROFILE.get();
        if (profile != null && profile.getWeaponCategory() != null) {
            return profile.getWeaponCategory();
        }
        return super.getWeaponCategory();
    }

    @Override
    public Collider getWeaponCollider() {
        ModifierProfile profile = CONTEXT_PROFILE.get();
        if (profile != null && profile.getCollider() != null) {
            return profile.getCollider();
        }
        return super.getWeaponCollider();
    }

    @Override
    public LivingMotion getLivingMotion(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        InteractionHand checkedHand = Objects.requireNonNull(hand, "hand");
        if (entityPatch instanceof PlayerPatch<?> playerPatch && playerPatch.getOriginal().isCrouching() && playerPatch.getOriginal().isUsingItem() && playerPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.SPEAR) {
            ItemStack itemStack = playerPatch.getOriginal().getItemInHand(checkedHand);
            ItemStack useItem = Objects.requireNonNull(playerPatch.getOriginal().getUseItem(), "useItem");
            if (!itemStack.isEmpty() && ItemStack.isSameItemSameTags(itemStack, useItem) && ToolStack.from(itemStack).getModifierLevel(Objects.requireNonNull(THROWING_ID, "throwing modifier id")) > 0) {
                return LivingMotions.AIM;
            }
        } 
        return super.getLivingMotion(entityPatch, checkedHand);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends WeaponCapability.Builder {
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
    }
}
