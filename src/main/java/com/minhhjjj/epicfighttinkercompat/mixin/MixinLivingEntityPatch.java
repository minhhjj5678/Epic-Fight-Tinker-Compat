package com.minhhjjj.epicfighttinkercompat.mixin;

import com.minhhjjj.epicfighttinkercompat.tool.capabilities.TCWeaponCapability;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = LivingEntityPatch.class, remap = false)
public abstract class MixinLivingEntityPatch {

    @Shadow(remap = false)
    public abstract CapabilityItem getAdvancedHoldingItemCapability(InteractionHand hand);

    @Inject(method = "getColliderMatching", at = @At("RETURN"), cancellable = true, remap = false)
    private void injectDynamicCollider(InteractionHand hand, CallbackInfoReturnable<Collider> cir) {
        CapabilityItem capabilityItem = this.getAdvancedHoldingItemCapability(hand);
        if (capabilityItem instanceof TCWeaponCapability toolCapability) {
            LivingEntityPatch<?> living = (LivingEntityPatch<?>) (Object) this;
            cir.setReturnValue(toolCapability.getWeaponCollider(living, hand));
        }
    }

}
