package com.minhhjjj.epicfighttinkercompat.gameasset;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.skill.ArrowTempestSkill;
import com.minhhjjj.epicfighttinkercompat.skill.SwapSkill;
import com.minhhjjj.epicfighttinkercompat.skill.TinkerRightClickSkill;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.weaponinnate.GuillotineAxeSkill;
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

    @SubscribeEvent
    public static void buildSkillEvent(SkillBuildEvent build) {
        SkillBuildEvent.ModRegistryWorker modRegistry = build.createRegistryWorker(EpicFightTinkerCompat.MODID);
        TINKER_RIGHT_CLICK_SKILL = modRegistry.build("tinker_right_click_skill", TinkerRightClickSkill::new, Skill.createBuilder().setCategory(SkillCategories.GUARD).setResource(Skill.Resource.NONE));
        SWAP_SKILL = modRegistry.build("swap_skill", SwapSkill::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setResource(Skill.Resource.NONE).setActivateType(Skill.ActivateType.ONE_SHOT));
        WeaponInnateSkill theGuillotine = (WeaponInnateSkill)modRegistry.build("arrow_tempest_skill", ArrowTempestSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(EFTAnimations.TEST).setResource(Skill.Resource.COOLDOWN));
        theGuillotine.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.5F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(20.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create(new float[0]))).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageTypeTags.WEAPON_INNATE));
        ARROW_TEMPEST_SKILL = theGuillotine;
    }

    private EFTSkills() {}
}
