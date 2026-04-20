package com.minhhjjj.epicfighttinkercompat.mixin.tool;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.modules.interaction.ThrowingModule;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = ThrowingModule.class, remap = false)
public class MixinThrowingModule {
    @Inject(method = "onToolUse", at = @At("HEAD"), cancellable = true)
    private void epicfighttinkercompat$blockStandingThrowUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource source, CallbackInfoReturnable<InteractionResult> cir) {
        PlayerPatch<?> playerPatch = EpicFightCapabilities.getPlayerPatch(player);
        if (playerPatch != null && playerPatch.isEpicFightMode() && !player.isCrouching()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
    private void epicfighttinkercompat$blockStandingThrow(IToolStackView tool, ModifierEntry modifier, LivingEntity living, int timeLeft, CallbackInfo ci) {
        if (living instanceof Player player && !player.isCrouching()) {
            PlayerPatch<?> playerPatch = EpicFightCapabilities.getPlayerPatch(player);
            if (playerPatch != null && playerPatch.isEpicFightMode()) {
                ci.cancel();
            }
        }
    }
}
