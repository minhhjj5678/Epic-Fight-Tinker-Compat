package com.minhhjjj.epicfighttinkercompat.mixin;

import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Inject(method = "getItemInHand", at = @At("RETURN"), cancellable = true)
    private void spoofingItem(InteractionHand hand, CallbackInfoReturnable<ItemStack> cir) {
        EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        if (!(this.getItemBySlot(slot).getItem() instanceof ModifiableItem)) return;

        if (!SkillToItemDictionary.isEmpty()) {
            cir.setReturnValue(SkillToItemDictionary.getItem());
        }
    }
}
