package com.minhhjjj.epicfighttinkercompat.mixin.skill.common;

import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = GuardSkill.class, remap = false)
public class MixinGuardSkill {
    @Unique
    private static final ModifierId EPICFIGHTTINKERCOMPAT$BLOCKING_ID = new ModifierId(TConstruct.MOD_ID, "blocking");

    @Inject(method = "isHoldingWeaponAvailable", at = @At("HEAD"), cancellable = true)
    private void epicfighttinkercompat$requireBlockingModifier(PlayerPatch<?> playerpatch, CapabilityItem itemCapability, GuardSkill.BlockType blockType, CallbackInfoReturnable<Boolean> cir) {
        if (itemCapability == null || playerpatch == null || playerpatch.getOriginal() == null) {
            return;
        }
        ItemStack stack = SkillToItemDictionary.getRealItemStack();
        if (stack == null || stack.isEmpty()) {
            stack = playerpatch.getOriginal().getMainHandItem();
        }

        if (stack.isEmpty() || !(stack.getItem() instanceof ModifiableItem)) {
            return;
        }

        ToolStack toolStack = ToolStack.from(stack);
        if (toolStack.getModifierLevel(EPICFIGHTTINKERCOMPAT$BLOCKING_ID) < 1) {
            cir.setReturnValue(false);
        }

        if (toolStack.getModifierList().stream().anyMatch(modifierEntry -> {
            Modifier modifier = modifierEntry.getModifier();
            return modifier instanceof GeneralInteractionModifierHook;}) && playerpatch.getOriginal().isCrouching()) {
            cir.setReturnValue(false);
        }
    }
}
