package com.minhhjjj.epicfighttinkercompat.gameasset;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.skill.SwapSkill;
import com.minhhjjj.epicfighttinkercompat.skill.TinkerRightClickSkill;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.passive.PassiveSkill;

@Mod.EventBusSubscriber(
        modid = EpicFightTinkerCompat.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class EFTSkills {
    public static Skill TINKER_RIGHT_CLICK_SKILL;
    public static Skill SWAP_SKILL;

    @SubscribeEvent
    public static void buildSkillEvent(SkillBuildEvent build) {
        SkillBuildEvent.ModRegistryWorker modRegistry = build.createRegistryWorker(EpicFightTinkerCompat.MODID);
        TINKER_RIGHT_CLICK_SKILL = modRegistry.build("tinker_right_click_skill", TinkerRightClickSkill::new, Skill.createBuilder().setCategory(SkillCategories.GUARD).setResource(Skill.Resource.NONE));
        SWAP_SKILL = modRegistry.build("swap_skill", SwapSkill::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setResource(Skill.Resource.NONE).setActivateType(Skill.ActivateType.ONE_SHOT));
    }

    private EFTSkills() {}
}
