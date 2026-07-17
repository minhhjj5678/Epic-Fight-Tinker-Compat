package com.minhhjjj.epicfighttinkercompat.gameasset;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.EFBowAnimations;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.damagesource.StunType;

import static com.minhhjjj.epicfighttinkercompat.skill.ArrowTempestSkill.shoot;

@Mod.EventBusSubscriber(
        modid = EpicFightTinkerCompat.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class EFTAnimations {
    public static AnimationManager.AnimationAccessor<StaticAnimation> BIPED_HOLD_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_WALK_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_RUN_HAMMER;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> HAMMER_AUTO1;

    public static AnimationManager.AnimationAccessor<AttackAnimation> TEST;

    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicFightTinkerCompat.MODID, EFTAnimations::build);
    }

    public static void build(AnimationManager.AnimationBuilder builder) {
        BIPED_HOLD_HAMMER = builder.nextAccessor("biped/living/hold_hammer", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED));
        BIPED_WALK_HAMMER = builder.nextAccessor("biped/living/walk_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
        BIPED_RUN_HAMMER = builder.nextAccessor("biped/living/run_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
        HAMMER_AUTO1 = builder.nextAccessor("biped/combat/hammer_auto1", (accessor) -> new BasicAttackAnimation(0.25f, 0.15f, 0.52f, 0.95f, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0f));
        TEST = builder.nextAccessor("biped/combat/arrow_tempest_skill",(accessor) -> new AttackAnimation(0.25f,
                0.15f, 0.52f, 0.95f, 0.95f, EFBowAnimations.BOW_SCAN, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED).addEvents(
                shoot(7.5F / 60, 0f), shoot(15F / 60, 45f), shoot(22.5F / 60, 90f),
                shoot(30F / 60, 135f), shoot(37.5F / 60, 180f), shoot(45F / 60, 225f),
                shoot(52.5F / 60, 270f), shoot(1, 315f)));
        EFBowAnimations.buildBowAnimations(builder);
    }
}
