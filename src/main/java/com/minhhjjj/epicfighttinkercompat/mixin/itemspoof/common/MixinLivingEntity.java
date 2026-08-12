package com.minhhjjj.epicfighttinkercompat.mixin.itemspoof.common;

import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import com.minhhjjj.epicfighttinkercompat.tool.ModifierProfileManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(method = "getItemInHand", at = @At("RETURN"), cancellable = true)
    private void spoofingItemInHand(InteractionHand hand, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(epicfighttinkercompat$checkAndSpoof(cir.getReturnValue()));
    }

    @Inject(method = "getMainHandItem", at = @At("RETURN"), cancellable = true)
    private void spoofingMainHandItem(CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(epicfighttinkercompat$checkAndSpoof(cir.getReturnValue()));
    }

    @Inject(method = "getOffhandItem", at = @At("RETURN"), cancellable = true)
    private void spoofingOffhandItem(CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(epicfighttinkercompat$checkAndSpoof(cir.getReturnValue()));
    }

    @Unique
    private ItemStack epicfighttinkercompat$checkAndSpoof(ItemStack original) {
        if ((original.getItem() instanceof ModifiableItem) && !SkillToItemDictionary.isEmpty()) {
            var modifierProfile = ModifierProfileManager.getProfile(original, ModifierProfileManager.WEAPON_FILTER);
            var spoofProfile = SkillToItemDictionary.getProfile();
            if (modifierProfile != null && spoofProfile.weaponType().equals(modifierProfile.weaponType())) {
                return new ItemStack(spoofProfile.spoofItem());
            }
        }

        return original;
    }

}
