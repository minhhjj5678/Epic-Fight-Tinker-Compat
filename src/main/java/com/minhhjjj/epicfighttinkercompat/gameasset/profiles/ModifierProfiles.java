package com.minhhjjj.epicfighttinkercompat.gameasset.profiles;


import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.gameasset.EFTSkills;
import com.minhhjjj.epicfighttinkercompat.modifiers.EpicFightModifiers;

import com.minhhjjj.epicfighttinkercompat.tool.capabilities.TCWeaponCapability;
import com.minhhjjj.epicfighttinkercompat.tool.capabilities.TCWeaponUtils;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.fml.ModList;
import reascer.wom.gameasset.WOMSkills;
import reascer.wom.gameasset.colliders.WOMWeaponColliders;
import reascer.wom.skill.WOMSkillDataKeys;
import reascer.wom.world.capabilities.item.WOMWeaponCategories;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModifierProfiles {
    public static final ModifierProfile TORMENT = ModList.get().isLoaded("wom") ? createTormentProfile() : null;
    public static final ModifierProfile RUINE = ModList.get().isLoaded("wom") ? createRuineProfile() : null;
    public static final ModifierProfile AGONY = ModList.get().isLoaded("wom") ? createAgonyProfile() : null;

    public static final ModifierProfile ARROW_TEMPEST = ModifierProfile.builder(EpicFightModifiers.ARROW_TEMPEST)
            .styleProvider(livingEntityPatch -> Styles.TWO_HAND)
            .innateSkill(EFTSkills.ARROW_TEMPEST_SKILL)
            .build();

    public static final ModifierProfile SPEARY = ModifierProfile.builder(EpicFightModifiers.SPEARY)
            .styleProvider((LivingEntityPatch<?> patch) -> {
                if (patch.getOriginal().getOffhandItem().isEmpty())
                    return Styles.TWO_HAND;
                else
                    return Styles.ONE_HAND;
            })
            .addMoveSet(Styles.ONE_HAND, CombatProfiles.spear1HSet().build())
            .addMoveSet(Styles.TWO_HAND, CombatProfiles.spear2HSet().build())
            .weaponCategory(WeaponCategories.SPEAR)
            .collider(ColliderPreset.SPEAR)
            .priority(50)
            .build();

    public static final ModifierProfile SWORD = ModifierProfile.builder(EpicFightModifiers.SHORTSWORD)
            .styleProvider((patch) -> {
                if (patch.getOriginal().isVehicle()) return Styles.MOUNT;
                if (TCWeaponUtils.getDynamicProperty(patch, patch.getHoldingItemCapability(InteractionHand.OFF_HAND), InteractionHand.OFF_HAND, CapabilityItem::getWeaponCategory, TCWeaponCapability::getWeaponCategory) == WeaponCategories.SWORD) return Styles.TWO_HAND;
                return Styles.ONE_HAND;
            })
            .addMoveSet(Styles.ONE_HAND, CombatProfiles.sword1HSet().build())
            .addMoveSet(Styles.TWO_HAND, CombatProfiles.sword2HSet().build())
            .addMoveSet(Styles.MOUNT, CombatProfiles.swordMountSet().build())
            .weaponCategory(WeaponCategories.SWORD)
            .priority(70)
            .build();

    public static final ModifierProfile LONGSWORD = ModifierProfile.builder(EpicFightModifiers.LONGSWORD)
            .styleProvider((patch) -> {
                if (patch instanceof PlayerPatch<?> pPatch) {
                    if (TCWeaponUtils.getDynamicProperty(pPatch, patch.getHoldingItemCapability(InteractionHand.OFF_HAND), InteractionHand.OFF_HAND, CapabilityItem::getWeaponCategory, TCWeaponCapability::getWeaponCategory) == WeaponCategories.SHIELD) return Styles.ONE_HAND;
                    if (pPatch.getSkill(SkillSlots.WEAPON_INNATE).isActivated()) return Styles.OCHS;
                }
                return Styles.TWO_HAND;
            })
            .addMoveSet(Styles.ONE_HAND, CombatProfiles.flamberge1HSet().build())
            .addMoveSet(Styles.TWO_HAND, CombatProfiles.flamberge2HSet().build())
            .addMoveSet(Styles.OCHS, CombatProfiles.flambergeOchsSet().build())
            .weaponCategory(WeaponCategories.LONGSWORD)
            .priority(80)
            .build();

    public static final ModifierProfile GREATSWORD = ModifierProfile.builder(EpicFightModifiers.GREATSWORD)
            .styleProvider((patch) -> Styles.TWO_HAND)
            .addMoveSet(Styles.TWO_HAND, CombatProfiles.cleaver2HSet().build())
            .weaponCategory(WeaponCategories.GREATSWORD)
            .priority(90)
            .build();

    public static final ModifierProfile DAGGER = ModifierProfile.builder(EpicFightModifiers.DAGGER)
            .styleProvider((patch) -> {
                if (TCWeaponUtils.getDynamicProperty(patch, patch.getHoldingItemCapability(InteractionHand.OFF_HAND), InteractionHand.OFF_HAND, CapabilityItem::getWeaponCategory, TCWeaponCapability::getWeaponCategory) == WeaponCategories.DAGGER) return Styles.TWO_HAND;
                return Styles.ONE_HAND;
            })
            .addMoveSet(Styles.ONE_HAND, CombatProfiles.dagger1HSet().build())
            .addMoveSet(Styles.TWO_HAND, CombatProfiles.dagger2HSet().build())
            .weaponCategory(WeaponCategories.DAGGER)
            .priority(60)
            .build();

    public static final ModifierProfile TACHI = ModifierProfile.builder(EpicFightModifiers.TACHI)
            .styleProvider((patch) -> Styles.TWO_HAND)
            .addMoveSet(Styles.TWO_HAND, CombatProfiles.tachiSet().build())
            .weaponCategory(WeaponCategories.TACHI)
            .priority(100)
            .build();

    public static final ModifierProfile UCHIGATANA = ModifierProfile.builder(EpicFightModifiers.UCHIGATANA)
            .styleProvider((patch) -> {
                if (patch instanceof PlayerPatch<?> playerPatch && playerPatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().hasData(SkillDataKeys.SHEATH.get()) && playerPatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(SkillDataKeys.SHEATH.get())) {
                    return Styles.SHEATH;
                }
                return Styles.TWO_HAND;
            })
            .addMoveSet(Styles.TWO_HAND, CombatProfiles.katanaBaseSet().build())
            .addMoveSet(Styles.SHEATH, CombatProfiles.katanaSheathedSet().build())
            .weaponCategory(WeaponCategories.UCHIGATANA)
            .priority(110)
            .build();

    public static final ModifierProfile AXE = ModifierProfile.builder(EpicFightModifiers.AXE)
            .styleProvider((patch) -> Styles.ONE_HAND)
            .addMoveSet(Styles.ONE_HAND, CombatProfiles.axe1HSet().build())
            .weaponCategory(WeaponCategories.AXE)
            .priority(55)
            .build();

    private static ModifierProfile createAgonyProfile() {
        return ModifierProfile.builder(EpicFightModifiers.AGONY_SPEAR)
                .styleProvider((LivingEntityPatch<?> patch) -> {
                    if (patch.getOriginal().onGround())
                        return Styles.TWO_HAND;
                    else {
                        return Styles.ONE_HAND;
                    }
                })
                .addMoveSet(Styles.TWO_HAND, CombatProfiles.agonyGroundSet().build())
                .addMoveSet(Styles.ONE_HAND, CombatProfiles.agonyAirSet().build())
                .weaponCategory(WOMWeaponCategories.AGONY)
                .collider(WOMWeaponColliders.AGONY)
                .priority(120)
                .build();
    }

    private static ModifierProfile createRuineProfile() {
        return ModifierProfile.builder(EpicFightModifiers.RUINE_BLADE)
                .styleProvider((LivingEntityPatch<?> patch) -> {
                    if (patch instanceof PlayerPatch<?> playerPatch) {
                        SkillDataManager dataManager = playerPatch.getSkill(SkillSlots.WEAPON_INNATE).getDataManager();
                        SkillDataKey<?> buffedKey = WOMSkillDataKeys.BUFFED.get();
                        Object buffed = dataManager.getDataValue(buffedKey);
                        return (buffed instanceof Boolean && (Boolean)buffed) ? Styles.OCHS : Styles.TWO_HAND;
                    }
                    return Styles.TWO_HAND;
                })
                .addMoveSet(Styles.TWO_HAND, CombatProfiles.ruine2HSet().build())
                .addMoveSet(Styles.OCHS, CombatProfiles.ruineOchsSet().build())
                .weaponCategory(WOMWeaponCategories.RUINE)
                .collider(WOMWeaponColliders.RUINE)
                .priority(130)
                .build();
    }

    private static ModifierProfile createTormentProfile() {
        return ModifierProfile.builder(EpicFightModifiers.TORMENT_BLADE)
                .styleProvider((LivingEntityPatch<?> patch) -> {
                    if (patch instanceof PlayerPatch<?> playerPatch) {
                        SkillContainer innateSkill = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
                        return (innateSkill.getRemainDuration() > 0 && innateSkill.getSkill() == WOMSkills.TRUE_BERSERK) ? Styles.OCHS : Styles.TWO_HAND;
                    }
                    return Styles.TWO_HAND;
                })
                .addMoveSet(Styles.TWO_HAND, CombatProfiles.torment2HSet().build())
                .addMoveSet(Styles.OCHS, CombatProfiles.tormentOchsSet().build())
                .weaponCategory(WOMWeaponCategories.TORMENT)
                .collider(WOMWeaponColliders.TORMENT)
                .priority(140)
                .build();
    }

    public static List<ModifierProfile> defaultModifierProfiles = new ArrayList<>(Arrays.asList(
            SWORD, LONGSWORD, GREATSWORD, TACHI, UCHIGATANA, AXE, DAGGER, SPEARY
    ));
}
