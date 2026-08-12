package com.minhhjjj.epicfighttinkercompat.mixin.itemspoof.common;

import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.AbstractPlayerEvent;
import yesman.epicfight.world.entity.eventlistener.DetachablePlayerEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

@Mixin(value = PlayerEventListener.class, remap = false)
public class MixinPlayerEventListener {

    @Inject(method = "triggerEvents", at = @At("HEAD"))
    private void addItemToBox(PlayerEventListener.EventType<? extends DetachablePlayerEvent<?>> eventType, DetachablePlayerEvent<?> event, CallbackInfoReturnable<Boolean> cir) {
        if (event instanceof AbstractPlayerEvent<?> playerEvent) {
            PlayerPatch<?> playerPatch = playerEvent.getPlayerPatch();
            SkillContainer innateSkill = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
            if (innateSkill != null && innateSkill.getSkill() != null) {
                SkillToItemDictionary.put(innateSkill.getSkill());
            }
        }
    }

    @Inject(method = "triggerEvents", at = @At("RETURN"))
    private void removeItemFromBox(PlayerEventListener.EventType<? extends DetachablePlayerEvent<?>> eventType, DetachablePlayerEvent<?> event, CallbackInfoReturnable<Boolean> cir) {
        SkillToItemDictionary.remove();
    }
}
