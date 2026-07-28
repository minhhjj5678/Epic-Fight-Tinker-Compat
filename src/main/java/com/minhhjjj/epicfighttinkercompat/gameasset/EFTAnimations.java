package com.minhhjjj.epicfighttinkercompat.gameasset;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.EFBowAnimations;
import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.TCScanAttackAnimation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.damagesource.StunType;

import static com.minhhjjj.epicfighttinkercompat.skill.bowinnate.ArrowTempestSkill.shoot;

@Mod.EventBusSubscriber(
        modid = EpicFightTinkerCompat.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class EFTAnimations {
    public static AnimationManager.AnimationAccessor<StaticAnimation> BIPED_HOLD_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_WALK_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_RUN_HAMMER;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> HAMMER_AUTO1;
    public static AnimationManager.AnimationAccessor<StaticAnimation> BIPED_HOLD_SWASHER;
    public static AnimationManager.AnimationAccessor<AimAnimation> BIPED_SWASHER_AIM;
    public static AnimationManager.AnimationAccessor<ReboundAnimation> BIPED_SWASHER_SHOT;

    public static AnimationManager.AnimationAccessor<AttackAnimation> ARROW_TEMPEST;
    public static AnimationManager.AnimationAccessor<AttackAnimation> SEEKING_TEMPEST;

    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicFightTinkerCompat.MODID, EFTAnimations::build);
    }

    public static void build(AnimationManager.AnimationBuilder builder) {
        BIPED_HOLD_HAMMER = builder.nextAccessor("biped/living/hold_hammer", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED));
        BIPED_WALK_HAMMER = builder.nextAccessor("biped/living/walk_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
        BIPED_RUN_HAMMER = builder.nextAccessor("biped/living/run_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
        HAMMER_AUTO1 = builder.nextAccessor("biped/combat/hammer_auto1", (accessor) -> new BasicAttackAnimation(0.25f, 0.15f, 0.52f, 0.95f, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0f));
        ARROW_TEMPEST = builder.nextAccessor("biped/combat/arrow_tempest_skill",(accessor) -> new TCScanAttackAnimation(0.25f,
                0f, 0.15f, 130/60f, 150/60f, null, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                .addProperty(AnimationProperty.AttackAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, living, defaultSpeed, v1, v2) -> {
                    ItemStack itemStack = living.getOriginal().getMainHandItem();
                    if (itemStack.getItem() instanceof ModifiableBowItem) {
                        ToolStack tool = ToolStack.from(itemStack);
                        return tool.getStats().get(ToolStats.DRAW_SPEED);
                    }
                    return 1.0F;
                })
                .addEvents(EFBowAnimations.setFullBowUseTime(50/60f), shoot(110F / 60)));
        SEEKING_TEMPEST = builder.nextAccessor("biped/combat/seeking_tempest_skill", (accessor) -> new TCScanAttackAnimation(
                0.15f,
                0f,
                0.15f,
                130/60f,
                150/60f,
                null,
                Armatures.BIPED.get().rootJoint,
                accessor,
                Armatures.BIPED
        )
                .addProperty(AnimationProperty.AttackAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, living, defaultSpeed, v1, v2) -> {
                        ItemStack itemStack = living.getOriginal().getMainHandItem();
                        if (itemStack.getItem() instanceof ModifiableBowItem) {
                            ToolStack tool = ToolStack.from(itemStack);
                            return tool.getStats().get(ToolStats.DRAW_SPEED);
                        }
                        return 1.0F;
                    })
                .addEvents(EFBowAnimations.setFullBowUseTime(50/60f), shoot(110/60f))
        );
        BIPED_HOLD_SWASHER = builder.nextAccessor("biped/living/hold_swasher", accessor -> new StaticAnimation(true, accessor, Armatures.BIPED));
        BIPED_SWASHER_AIM = builder.nextAccessor("biped/combat/swasher_aim",
                accessor -> new AimAnimation(
                        false,
                        accessor,
                        "biped/combat/swasher_aim_mid",
                        "biped/combat/swasher_aim_up",
                        "biped/combat/swasher_aim_down",
                        "biped/combat/swasher_aim_lying",
                        Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (AnimationProperty.PlaybackSpeedModifier)(animation, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
                    if (animation.isLinkAnimation()) {
                        return 1.0F;
                    } else {
                        return (entitypatch.getOriginal()).isUsingItem() && elapsedTime + 0.05F * speed > animation.getTotalTime() ? 0.0F : 1.0F;
                    }
                }));
        BIPED_SWASHER_SHOT = builder.nextAccessor("biped/combat/swasher_shot",
                accessor -> new ReboundAnimation(
                        false,
                        accessor,
                        "biped/combat/swasher_shot_mid",
                        "biped/combat/swasher_shot_up",
                        "biped/combat/swasher_shot_down",
                        "biped/combat/swasher_shot_lying",
                        Armatures.BIPED
                ));


        EFBowAnimations.buildBowAnimations(builder);

    }
}
