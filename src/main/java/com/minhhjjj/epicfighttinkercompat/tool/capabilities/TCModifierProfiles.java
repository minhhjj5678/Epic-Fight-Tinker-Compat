package com.minhhjjj.epicfighttinkercompat.tool.capabilities;


import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.modifiers.EpicFightModifiers;

import net.minecraftforge.fml.ModList;
import reascer.wom.gameasset.WOMSkills;
import reascer.wom.gameasset.colliders.WOMWeaponColliders;
import reascer.wom.skill.WOMSkillDataKeys;
import reascer.wom.world.capabilities.item.WOMWeaponCategories;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;

public class TCModifierProfiles {
    private static final ModifierId SPEARY_ID = new ModifierId(EpicFightTinkerCompat.MODID, "speary");

    public static final ModifierProfile TORMENT = ModList.get().isLoaded("wom") ? createTormentProfile() : null;
    public static final ModifierProfile RUINE = ModList.get().isLoaded("wom") ? createRuineProfile() : null;
    public static final ModifierProfile AGONY = ModList.get().isLoaded("wom") ? createAgonyProfile() : null;
    public static final ModifierProfile SPEARY = ModifierProfile.builder(SPEARY_ID)
            .styleProvider((LivingEntityPatch<?> patch) -> {
                if (patch.getOriginal().getOffhandItem().isEmpty())
                    return Styles.TWO_HAND;
                else
                    return Styles.ONE_HAND;
            })
            .addMoveSet(Styles.ONE_HAND, TCMoveSets.spear1HSet().build())
            .addMoveSet(Styles.TWO_HAND, TCMoveSets.spear2HSet().build())
            .weaponCategory(WeaponCategories.SPEAR)
            .collider(ColliderPreset.SPEAR)
            .priority(50)
            .build();

    private static ModifierProfile createAgonyProfile() {
        return ModifierProfile.builder(EpicFightModifiers.AGONY_SPEAR.getId())
                .styleProvider((LivingEntityPatch<?> patch) -> {
                    if (patch.getOriginal().onGround())
                        return Styles.TWO_HAND;
                    else {
                        return Styles.ONE_HAND;
                    }
                })
                .addMoveSet(Styles.TWO_HAND, TCMoveSets.agonyGroundSet().build())
                .addMoveSet(Styles.ONE_HAND, TCMoveSets.agonyAirSet().build())
                .weaponCategory(WOMWeaponCategories.AGONY)
                .collider(WOMWeaponColliders.AGONY)
                .priority(100)
                .build();
    }

    private static ModifierProfile createRuineProfile() {
        return ModifierProfile.builder(EpicFightModifiers.RUINE_BLADE.getId())
                .styleProvider((LivingEntityPatch<?> patch) -> {
                    if (patch instanceof PlayerPatch<?> playerPatch) {
                        SkillDataManager dataManager = playerPatch.getSkill(SkillSlots.WEAPON_INNATE).getDataManager();
                        SkillDataKey<?> buffedKey = WOMSkillDataKeys.BUFFED.get();
                        Object buffed = dataManager.getDataValue(buffedKey);
                        return (buffed instanceof Boolean && (Boolean)buffed) ? Styles.OCHS : Styles.TWO_HAND;
                    }
                    return Styles.TWO_HAND;
                })
                .addMoveSet(Styles.TWO_HAND, TCMoveSets.ruine2HSet().build())
                .addMoveSet(Styles.OCHS, TCMoveSets.ruineOchsSet().build())
                .weaponCategory(WOMWeaponCategories.RUINE)
                .collider(WOMWeaponColliders.RUINE)
                .priority(100)
                .build();
    }

    private static ModifierProfile createTormentProfile() {
        return ModifierProfile.builder(EpicFightModifiers.TORMENT_BLADE.getId())
                .styleProvider((LivingEntityPatch<?> patch) -> {
                    if (patch instanceof PlayerPatch<?> playerPatch) {
                        SkillContainer innateSkill = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
                        return (innateSkill.getRemainDuration() > 0 && innateSkill.getSkill() == WOMSkills.TRUE_BERSERK) ? Styles.OCHS : Styles.TWO_HAND;
                    }
                    return Styles.TWO_HAND;
                })
                .addMoveSet(Styles.TWO_HAND, TCMoveSets.torment2HSet().build())
                .addMoveSet(Styles.OCHS, TCMoveSets.tormentOchsSet().build())
                .weaponCategory(WOMWeaponCategories.TORMENT)
                .collider(WOMWeaponColliders.TORMENT)
                .priority(100)
                .build();
    }
}
