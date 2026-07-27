package com.minhhjjj.epicfighttinkercompat.mixin.tool;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import com.minhhjjj.epicfighttinkercompat.tool.capabilities.TCWeaponCapability;

@Mixin(value = CapabilityItem.class, remap = false)
public class MixinCapabilityItem {

    @ModifyExpressionValue(
            method = "changeWeaponInnateSkill",
            at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/capabilities/item/CapabilityItem;getPassiveSkill()Lyesman/epicfight/skill/Skill;")
    )
    private Skill injectCustomPassiveSkill(Skill originalSkill, @Local(argsOnly = true) PlayerPatch<?> playerPatch) {
        if ((Object) this instanceof TCWeaponCapability tcCap) {
            return tcCap.getPassiveSkill(playerPatch);
        }
        return originalSkill;
    }
}