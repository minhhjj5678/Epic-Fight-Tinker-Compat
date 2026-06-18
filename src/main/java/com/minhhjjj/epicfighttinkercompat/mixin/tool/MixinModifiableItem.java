package com.minhhjjj.epicfighttinkercompat.mixin.tool;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
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
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import static com.minhhjjj.epicfighttinkercompat.modifiers.EpicFightModifiers.BLOCKING;

@Mixin(value = ModifiableItem.class)
public class MixinModifiableItem {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void epicfighttinkercompat$cancelUseInEpicFightMode(Level worldIn, Player playerIn, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        PlayerPatch<?> playerPatch = EpicFightCapabilities.getPlayerPatch(playerIn);
        if (playerPatch != null && playerPatch.isEpicFightMode()) {
            ToolStack toolStack = ToolStack.from(playerIn.getItemInHand(hand));
            if (playerPatch.getSkill(SkillSlots.GUARD).getSkill() instanceof GuardSkill && toolStack.getModifierLevel(BLOCKING) > 0 && !playerIn.isCrouching()) {
                if (!toolStack.getDefinition().getId().getPath().contains("shield"))
                    cir.setReturnValue(InteractionResultHolder.consume(playerIn.getItemInHand(hand)));
            }
        }
    }
}
