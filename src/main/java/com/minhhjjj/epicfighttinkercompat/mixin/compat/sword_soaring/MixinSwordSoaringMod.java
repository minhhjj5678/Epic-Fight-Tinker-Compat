package com.minhhjjj.epicfighttinkercompat.mixin.compat.sword_soaring;

import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(targets = "net.p1nero.ss.SwordSoaringMod", remap = false)
public class MixinSwordSoaringMod {

    @Inject(method = "isValidSword", at = @At("RETURN"), cancellable = true)
    private static void epicfighttinkercompat$isValidTinkerSword(ItemStack sword, CallbackInfoReturnable<Boolean> cir) {
        ItemStack realItem = SkillToItemDictionary.getRealItemStack();
        if (realItem == null || realItem.isEmpty()) {
            realItem = sword;
        }

        if (!cir.getReturnValue() && realItem.getItem() instanceof IModifiable) {
            ToolStack tool = ToolStack.from(realItem);
            if (tool.isBroken()) {
                return;
            }

            CapabilityItem cap = EpicFightCapabilities.getItemStackCapability(realItem);
            if (cap != null && cap.getWeaponCategory() == CapabilityItem.WeaponCategories.SWORD) {
                cir.setReturnValue(true);
            }
        }
    }
}
