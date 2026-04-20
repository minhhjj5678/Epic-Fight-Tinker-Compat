package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.HashMap;
import java.util.Map;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.modifiers.EpicFightModifiers;

import net.minecraftforge.fml.ModList;
import reascer.wom.gameasset.WOMSkills;
import reascer.wom.gameasset.colliders.WOMWeaponColliders;
import reascer.wom.skill.WOMSkillDataKeys;
import reascer.wom.world.capabilities.item.WOMWeaponCategories;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import yesman.epicfight.api.ex_cap.core.data.MoveSet;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.capabilities.item.Style;

public class TCModifierProfiles {
    private static final ModifierId SPEARY_ID = new ModifierId(EpicFightTinkerCompat.MODID, "speary");

    public static final ModifierProfile TORMENT = ModList.get().isLoaded("wom") ? createTormentProfile() : null;
    public static final ModifierProfile RUINE = ModList.get().isLoaded("wom") ? createRuineProfile() : null;
    public static final ModifierProfile AGONY = ModList.get().isLoaded("wom") ? createAgonyProfile() : null;
    public static final ModifierProfile SPEARY = new ModifierProfile(
        SPEARY_ID,
        (LivingEntityPatch<?> patch) -> {
            if (patch.getOriginal().getOffhandItem().isEmpty())
                return Styles.TWO_HAND;
            else
                return Styles.ONE_HAND;
        },
        createSpearMS(),
        WeaponCategories.SPEAR,
        ColliderPreset.SPEAR,
        50
    );


    private static Map<Style, MoveSet> createSpearMS() {
        Map<Style, MoveSet> moveSets = new HashMap<>();
        moveSets.put(Styles.ONE_HAND, TCMoveSets.spear1HSet().build());
        moveSets.put(Styles.TWO_HAND, TCMoveSets.spear2HSet().build());
        return moveSets;
    }

    private static ModifierProfile createAgonyProfile() {
        Map<Style, MoveSet> moveSets = new HashMap<>();
        moveSets.put(Styles.TWO_HAND, TCMoveSets.agonyGroundSet().build());
        moveSets.put(Styles.ONE_HAND, TCMoveSets.agonyAirSet().build());
        return new ModifierProfile(
            EpicFightModifiers.AGONY_SPEAR.getId(),
            (LivingEntityPatch<?> patch) -> {
                if (patch.getOriginal().onGround())
                    return Styles.TWO_HAND;
                else {
                    return Styles.ONE_HAND;
                }
            },
            moveSets,
            WOMWeaponCategories.AGONY,
            WOMWeaponColliders.AGONY,
            100
        );
    }

    private static ModifierProfile createRuineProfile() {
        Map<Style, MoveSet> moveSets = new HashMap<>();
        moveSets.put(Styles.TWO_HAND, TCMoveSets.ruine2HSet().build());
        moveSets.put(Styles.OCHS, TCMoveSets.ruineOchsSet().build());
        return new ModifierProfile(
            EpicFightModifiers.RUINE_BLADE.getId(),
            (LivingEntityPatch<?> patch) -> {
                if (patch instanceof PlayerPatch playerPatch) {
                    SkillDataManager dataManager = playerPatch.getSkill(SkillSlots.WEAPON_INNATE).getDataManager();
                    SkillDataKey<?> buffedKey = (SkillDataKey<?>)WOMSkillDataKeys.BUFFED.get();
                    Object buffed = dataManager.getDataValue(buffedKey);
                    return (buffed instanceof Boolean && (Boolean)buffed) ? Styles.OCHS : Styles.TWO_HAND;
                }
                return Styles.TWO_HAND;
            },
            moveSets,
            WOMWeaponCategories.RUINE,
            WOMWeaponColliders.RUINE,
            100
        );
    }

    private static ModifierProfile createTormentProfile() {
        Map<Style, MoveSet> moveSets = new HashMap<>();
        moveSets.put(Styles.TWO_HAND, TCMoveSets.torment2HSet().build());
        moveSets.put(Styles.OCHS, TCMoveSets.tormentOchsSet().build());
        return new ModifierProfile(
            EpicFightModifiers.TORMENT_BLADE.getId(),
            (LivingEntityPatch<?> patch) -> {
                if (patch instanceof PlayerPatch<?> playerPatch) {
                    SkillContainer innateSkill = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
                    return (innateSkill.getRemainDuration() > 0 && innateSkill.getSkill() == WOMSkills.TRUE_BERSERK) ? Styles.OCHS : Styles.TWO_HAND;
                }
                return Styles.TWO_HAND;
            },
            moveSets,
            WOMWeaponCategories.TORMENT,
            WOMWeaponColliders.TORMENT,
            100
        );
    }
}
