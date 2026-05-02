package com.minhhjjj.epicfighttinkercompat.mixin.tool;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = ModifiableItem.class, remap = false)
public class MixinModifiableItem {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void epicfighttinkercompat$cancelUseInEpicFightMode(Level worldIn, Player playerIn, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        PlayerPatch<?> playerPatch = EpicFightCapabilities.getPlayerPatch(playerIn);
        if (playerPatch != null && playerPatch.isEpicFightMode()) {
            cir.setReturnValue(InteractionResultHolder.consume(playerIn.getItemInHand(hand)));
        }
    }
}
