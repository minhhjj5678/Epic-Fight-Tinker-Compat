package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.*;

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
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.guard.GuardSkill;
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
    private static final ModifierId BLOCKING_ID = new ModifierId(TConstruct.MOD_ID, "blocking");

    protected TCWeaponCapability(CapabilityItem.Builder builder) {
        super(builder);
        Builder tcBuilder = (Builder)builder;
        this.defaultMoveSet = tcBuilder.defaultMoveSet;
        this.modifierProfiles = tcBuilder.modifierProfiles;
        sortModifierProfiles();
    }

    @Override
    public MoveSet getCurrentSet(LivingEntityPatch<?> patch) {
        ModifierProfile modifierProfile = this.getModifierProfile(patch);
        if (modifierProfile != null) {
            Style resolvedStyle = modifierProfile.styleProvider().apply(patch);
            MoveSet moveSet = modifierProfile.moveSets().get(resolvedStyle);
            if (moveSet != null) {
                return moveSet;
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
        ModifierProfile modifierProfile = this.getModifierProfile();
        if (modifierProfile != null && modifierProfile.weaponCategory() != null) {
            return modifierProfile.weaponCategory();
        }
        return super.getWeaponCategory();
    }

    @Override
    public Collider getWeaponCollider() {
        ModifierProfile modifierProfile = this.getModifierProfile();
        if (modifierProfile != null && modifierProfile.collider() != null) {
            return modifierProfile.collider();
        }
        return super.getWeaponCollider();
    }

    @Override
    public LivingMotion getLivingMotion(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        InteractionHand checkedHand = Objects.requireNonNull(hand, "hand");
        if (entityPatch instanceof PlayerPatch<?> playerPatch && playerPatch.getOriginal().isCrouching() && playerPatch.getOriginal().isUsingItem() && playerPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.SPEAR) {
            ItemStack heldItem = playerPatch.getOriginal().getItemInHand(checkedHand);
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
        ToolStack toolStack = getToolStack(entityPatch);
        if (set != null && set.getLivingMotionModifiers().containsKey(LivingMotions.BLOCK) && toolStack != null && toolStack.getModifierLevel(Objects.requireNonNull(BLOCKING_ID, "blocking modifier id")) > 0) {
            return UseAnim.BLOCK;
        }
        return UseAnim.NONE;
    }

    @Override
    public Skill getPassiveSkill(PlayerPatch<?> playerPatch) {
        MoveSet set = getCurrentSet(playerPatch);
        if (set != null) {
            return set.getWeaponPassiveSkill();
        }
        return getPassiveSkill();
    }

    public Skill getPassiveSkill() {
        ModifierProfile modifierProfile = this.getModifierProfile();
        return modifierProfile != null ? modifierProfile.passiveSkill() : null;
    }

    @SuppressWarnings("null")
    protected ToolStack getToolStack(LivingEntityPatch<?> entityPatch) {
        if (entityPatch != null) {
            ItemStack itemStack = entityPatch.getOriginal().isUsingItem() ? entityPatch.getOriginal().getItemInHand(entityPatch.getOriginal().getUsedItemHand()) : entityPatch.getOriginal().getMainHandItem();
            return itemStack.isEmpty() ? null : ToolStack.from(itemStack);
        }
        return null;
    }

    protected ModifierProfile getModifierProfile(LivingEntityPatch<?> entityPatch) {
        ToolStack toolStack = getToolStack(entityPatch);
        if (toolStack != null) {
            for (ModifierProfile modifierProfile : this.modifierProfiles) {
                ModifierId modifierId = modifierProfile.modifierId();
                if (modifierId == null || modifierProfile.moveSets().isEmpty()) {
                    continue;
                }

                int level = toolStack.getModifierLevel(modifierId);
                if (level > 0) {
                    return modifierProfile;
                }
            }
        }
        return null;
    }

    protected ModifierProfile getModifierProfile() {
        return this.getModifierProfile(null);
    }

    protected void sortModifierProfiles() {
        this.warnDuplicatePriorities();
        this.modifierProfiles.sort((first, second) -> {
            if (first.modifierId() == null && second.modifierId() == null) {
                return 0;
            }
            if (first.modifierId() == null) {
                return 1;
            }
            if (second.modifierId() == null) {
                return -1;
            }
            return Integer.compare(second.priority(), first.priority());
        });
    }

    protected void warnDuplicatePriorities() {
        Map<Integer, List<ModifierProfile>> groupedByPriority = new HashMap<>();
        for (ModifierProfile modifierProfile : this.modifierProfiles) {
            groupedByPriority.computeIfAbsent(modifierProfile.priority(), key -> new ArrayList<>()).add(modifierProfile);
        }

        for (Map.Entry<Integer, List<ModifierProfile>> entry : groupedByPriority.entrySet()) {
            List<ModifierProfile> profiles = entry.getValue();
            if (profiles.size() < 2) {
                continue;
            }
            String profileList = profiles.stream().map(profile -> String.valueOf(profile.modifierId())).collect(java.util.stream.Collectors.joining(", "));
            com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat.LOGGER.warn("Duplicate TC modifier priority {} detected for profiles: {}", entry.getKey(), profileList);
        }
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
