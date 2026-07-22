package com.minhhjjj.epicfighttinkercompat.gameasset;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.skill.ArrowTempestSkill;
import com.minhhjjj.epicfighttinkercompat.skill.SeekingTempestSkill;
import com.minhhjjj.epicfighttinkercompat.skill.SwapSkill;
import com.minhhjjj.epicfighttinkercompat.skill.TinkerRightClickSkill;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Set;

@Mod.EventBusSubscriber(
        modid = EpicFightTinkerCompat.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class EFTSkills {
    public static Skill TINKER_RIGHT_CLICK_SKILL;
    public static Skill SWAP_SKILL;
    public static Skill ARROW_TEMPEST_SKILL;
    public static Skill SEEKING_TEMPEST_SKILL;

    @SubscribeEvent
    public static void buildSkillEvent(SkillBuildEvent build) {
        SkillBuildEvent.ModRegistryWorker modRegistry = build.createRegistryWorker(EpicFightTinkerCompat.MODID);
        TINKER_RIGHT_CLICK_SKILL = modRegistry.build("tinker_right_click_skill", TinkerRightClickSkill::new, Skill.createBuilder().setCategory(SkillCategories.GUARD).setResource(Skill.Resource.NONE));
        SWAP_SKILL = modRegistry.build("swap_skill", SwapSkill::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setResource(Skill.Resource.NONE).setActivateType(Skill.ActivateType.ONE_SHOT));
        WeaponInnateSkill arrowTempestSkill = modRegistry.build("arrow_tempest", ArrowTempestSkill::new, WeaponInnateSkill.createWeaponInnateBuilder().setResource(Skill.Resource.COOLDOWN));
        arrowTempestSkill.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageTypeTags.WEAPON_INNATE));
        ARROW_TEMPEST_SKILL = arrowTempestSkill;

        SEEKING_TEMPEST_SKILL = modRegistry.build("seeking_tempest", SeekingTempestSkill::new, WeaponInnateSkill.createWeaponInnateBuilder().setResource(Skill.Resource.COOLDOWN)).newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageTypeTags.WEAPON_INNATE));

    }

    private EFTSkills() {}
}
