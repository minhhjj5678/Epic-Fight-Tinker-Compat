package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.EFBowAnimations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ArrowTempestSkill extends SimpleWeaponInnateSkill {
    private static final UUID EVENT_UUID = UUID.fromString("c9e8b1d4-6f2a-47bd-98f1-1d6c5a72e4b9");
    private static final ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "textures/gui/skills/weapon_innate/steel_whirlwind.png");
    public static final float DEFAULT_COOLDOWN = 10f;
    public static final String KEY_ARROW_TEMPEST_COOLDOWN = "arrow_tempest_cooldown";

    public ArrowTempestSkill(SimpleWeaponInnateSkill.Builder builder) {
        super(builder);
    }

    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID, (event) -> {
            if (!(container.getExecutor().getOriginal() instanceof ServerPlayer serverPlayer)) return;
//            if (Arrays.stream(EFBowAnimations.getComboAttack()).anyMatch(a -> a.get().equals(event.getDamageSource().getAnimation()))) {
            if (List.of(EFBowAnimations.BOW_ATTACK).contains(event.getDamageSource().getAnimation())) {
                if (!serverPlayer.getPersistentData().contains(KEY_ARROW_TEMPEST_COOLDOWN)) {
                    serverPlayer.getPersistentData().putFloat(KEY_ARROW_TEMPEST_COOLDOWN, DEFAULT_COOLDOWN);
                }
                serverPlayer.getPersistentData().putFloat(KEY_ARROW_TEMPEST_COOLDOWN, serverPlayer.getPersistentData().getFloat(KEY_ARROW_TEMPEST_COOLDOWN) - 1);
            } else {
                serverPlayer.getPersistentData().putFloat(KEY_ARROW_TEMPEST_COOLDOWN, serverPlayer.getPersistentData().getFloat(KEY_ARROW_TEMPEST_COOLDOWN) - 5);
            }
        });
    }

    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID);
    }

    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
    }

    public ResourceLocation getSkillTexture() {
        return resourceLocation;
    }

}
