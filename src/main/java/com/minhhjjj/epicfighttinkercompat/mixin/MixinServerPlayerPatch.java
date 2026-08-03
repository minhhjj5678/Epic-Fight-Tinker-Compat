package com.minhhjjj.epicfighttinkercompat.mixin;

import com.minhhjjj.epicfighttinkercompat.events.ShootingAnimationPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(value = {ServerPlayerPatch.class}, remap = false)
public abstract class MixinServerPlayerPatch {

    @Inject(method = "modifyLivingMotionByCurrentItem(Z)V", at = @At("HEAD"), cancellable = true)
    private void tinkercompat$preventResetWhileShooting(boolean checkOldAnimations, CallbackInfo ci) {
        ServerPlayerPatch patch = ((ServerPlayerPatch) ((Object) this));
        if (ShootingAnimationPlayer.consumeFlag(patch.getOriginal())) {
            ci.cancel();
        }
    }
}