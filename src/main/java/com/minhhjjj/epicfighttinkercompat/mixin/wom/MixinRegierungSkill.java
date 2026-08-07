package com.minhhjjj.epicfighttinkercompat.mixin.wom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reascer.wom.skill.weaponinnate.RegierungSkill;
import reascer.wom.world.capabilities.item.GesetzCapability;
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
    private static final ModifierId tinkercompat$herrscher = new ModifierId("epicfighttinkercompat", "herrscher");

    @Inject(method = "canExecute", at = @At("RETURN"), cancellable = true)
    private void tinkercompat$canExecuteOnTool(SkillContainer container, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            ItemStack mainHand = container.getExecutor().getOriginal().getMainHandItem();
            ItemStack offHand = container.getExecutor().getOriginal().getOffhandItem();

            boolean mainHandValid = false;
            if (mainHand.getItem() instanceof IModifiable) {
                ToolStack mainHandTool = ToolStack.from(mainHand);
                mainHandValid = !mainHandTool.isBroken() && mainHandTool.getModifierLevel(tinkercompat$herrscher) > 0;
            }

            boolean offhandValid = container.getExecutor().getHoldingItemCapability(InteractionHand.OFF_HAND) instanceof GesetzCapability;
            if (!offhandValid && offHand.getItem() instanceof IModifiable) {
                ToolStack offhandTool = ToolStack.from(offHand);
                offhandValid = !offhandTool.isBroken() && offhandTool.getModifierLevel(tinkercompat$gesetz) > 0;
            }

            if (mainHandValid && offhandValid) {
                cir.setReturnValue(true);
            }
        }
    }

}
