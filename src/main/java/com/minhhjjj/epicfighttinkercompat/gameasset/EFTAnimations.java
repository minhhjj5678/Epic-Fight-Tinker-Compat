package com.minhhjjj.epicfighttinkercompat.gameasset;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.JointMaskEntry;
import yesman.epicfight.gameasset.Armatures;

@Mod.EventBusSubscriber(
        modid = EpicFightTinkerCompat.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class EFTAnimations {
    public static AnimationManager.AnimationAccessor<StaticAnimation> BIPED_HOLD_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_WALK_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_RUN_HAMMER;

    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicFightTinkerCompat.MODID, EFTAnimations::build);
    }

    public static void build(AnimationManager.AnimationBuilder builder) {
        BIPED_HOLD_HAMMER = builder.nextAccessor("biped/living/hold_hammer", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED));
        BIPED_WALK_HAMMER = builder.nextAccessor("biped/living/walk_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
        BIPED_RUN_HAMMER = builder.nextAccessor("biped/living/run_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
    }
}
