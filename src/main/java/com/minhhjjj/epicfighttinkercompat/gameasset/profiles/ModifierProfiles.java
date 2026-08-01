package com.minhhjjj.epicfighttinkercompat.gameasset.profiles;


import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.EFBowAnimations;
import com.minhhjjj.epicfighttinkercompat.gameasset.EFTSkills;
import com.minhhjjj.epicfighttinkercompat.modifiers.EpicFightModifiers;

import net.minecraftforge.fml.ModList;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.skill.*;

public class ModifierProfiles {

    public static final ModifierProfile EAGLE_EYE = ModifierProfile.builder(EpicFightModifiers.EAGLE_EYE)
            .collider(tool -> {
                int level = tool.getModifierLevel(EpicFightModifiers.EAGLE_EYE);
                Collider collider = ColliderPreset.FIST;

                if (ModList.get().isLoaded("p1nero_bow")) {
                    if (level == 1) collider = EFBowAnimations.BOW_SCAN_LEVEL1;
                    else if (level == 2) collider = EFBowAnimations.BOW_SCAN_LEVEL2;
                    else if (level == 3) collider = EFBowAnimations.BOW_SCAN_LEVEL3;
                    else collider = EFBowAnimations.BOW_SCAN_LEVEL0;
                }

                return collider;
            })
            .build();

    public static final ModifierProfile ARROW_TEMPEST = ModifierProfile.builder(EpicFightModifiers.ARROW_TEMPEST)
            .innateSkill(((tool, playerPatch) -> tool.getModifierLevel(EpicFightModifiers.ARROW_TEMPEST) > 1 ? EFTSkills.SEEKING_TEMPEST_SKILL : (tool.getModifierLevel(EpicFightModifiers.ARROW_TEMPEST) > 0 ? EFTSkills.ARROW_TEMPEST_SKILL : null)))
            .build();
}
