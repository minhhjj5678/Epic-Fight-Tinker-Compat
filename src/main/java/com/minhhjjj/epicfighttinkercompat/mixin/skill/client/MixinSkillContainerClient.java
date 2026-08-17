package com.minhhjjj.epicfighttinkercompat.mixin.skill.client;

import com.minhhjjj.epicfighttinkercompat.skill.PersistentWeaponInnateSkill;
import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;

@Mixin(value = SkillContainer.class, remap = false)
public class MixinSkillContainerClient {

    @Shadow
    protected int stack;

    @Shadow
    protected Skill containingSkill;

    @Redirect(
            method = "setSkillRemote(Lyesman/epicfight/skill/Skill;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lyesman/epicfight/skill/SkillContainer;stack:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void preventResetStackRemote(SkillContainer container, int originalValue) {
        if (!(containingSkill instanceof PersistentWeaponInnateSkill)) {
            stack = originalValue;
        }
    }

    @Inject(method = "sendCastRequest", at = @At("HEAD"))
    private void addCastRequest(CallbackInfoReturnable<SkillCastEvent> ci) {
        if (containingSkill != null) {
            Player player = ((SkillContainer) ((Object) this)).getExecutor().getOriginal();
            ItemStack realItem = ItemStack.EMPTY;
            if (player != null) {
                realItem = player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem();
            }
            SkillToItemDictionary.put(containingSkill, realItem);
        }
    }

    @Inject(method = "sendCastRequest", at = @At("RETURN"))
    private void removeCastRequest(CallbackInfoReturnable<SkillCastEvent> ci) {
        SkillToItemDictionary.remove();
    }

}
