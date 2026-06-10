package com.minhhjjj.epicfighttinkercompat.gameasset;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.JointMaskEntry;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.damagesource.StunType;

@Mod.EventBusSubscriber(
        modid = EpicFightTinkerCompat.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class EFTAnimations {
    public static AnimationManager.AnimationAccessor<StaticAnimation> BIPED_HOLD_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_WALK_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_RUN_HAMMER;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> HAMMER_AUTO1;

    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicFightTinkerCompat.MODID, EFTAnimations::build);
    }

    public static void build(AnimationManager.AnimationBuilder builder) {
        BIPED_HOLD_HAMMER = builder.nextAccessor("biped/living/hold_hammer", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED));
        BIPED_WALK_HAMMER = builder.nextAccessor("biped/living/walk_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
        BIPED_RUN_HAMMER = builder.nextAccessor("biped/living/run_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
        HAMMER_AUTO1 = builder.nextAccessor("biped/combat/hammer_auto1", (accessor) -> new BasicAttackAnimation(0.25f, 0.35f, 0.52f, 0.95f, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.8f));
    }
}
