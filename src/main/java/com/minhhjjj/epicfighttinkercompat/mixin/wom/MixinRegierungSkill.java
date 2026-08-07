package com.minhhjjj.epicfighttinkercompat.mixin.wom;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reascer.wom.skill.weaponinnate.RegierungSkill;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.skill.SkillContainer;

@Pseudo
@Mixin(value = {RegierungSkill.class}, remap = false)
public class MixinRegierungSkill {

    @Unique
    private static final ModifierId tinkercompat$gesetz = new ModifierId("epicfighttinkercompat", "gesetz");

    @Unique
    private static final ModifierId tinkercompat$herrscher = new ModifierId("epicfighttinkercompat", "hercscher");

    @Inject(method = "canExecute", at = @At("RETURN"))
    private void tinkercompat$canExecuteOnTool(SkillContainer container, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            ItemStack mainHand = container.getExecutor().getOriginal().getMainHandItem();
            ItemStack offHand = container.getExecutor().getOriginal().getOffhandItem();
            if (mainHand.getItem() instanceof IModifiable && offHand.getItem() instanceof IModifiable) {
                ToolStack mainHandTool = ToolStack.from(mainHand);
                ToolStack offHandTool = ToolStack.from(offHand);
                if (!offHandTool.isBroken() && !mainHandTool.isBroken() && offHandTool.getModifierLevel(tinkercompat$gesetz) > 0
                && mainHandTool.getModifierLevel(tinkercompat$herrscher) > 0) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

}
