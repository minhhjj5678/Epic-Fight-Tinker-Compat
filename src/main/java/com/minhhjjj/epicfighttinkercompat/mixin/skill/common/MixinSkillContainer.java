package com.minhhjjj.epicfighttinkercompat.mixin.skill.common;

import com.minhhjjj.epicfighttinkercompat.skill.PersistentWeaponInnateSkill;
import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;

@Mixin(value = SkillContainer.class, remap = false)
public abstract class MixinSkillContainer {

    @Shadow
    protected int stack;

    @Shadow
    protected Skill containingSkill;

    @Redirect(
        method = "setSkill(Lyesman/epicfight/skill/Skill;Z)Z",
        at = @At(
            value = "FIELD",
            target = "Lyesman/epicfight/skill/SkillContainer;stack:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void preventResetStack(SkillContainer container, int originalValue) {
        if (!(containingSkill instanceof PersistentWeaponInnateSkill)) {
            stack = originalValue;
        }
    }

    @Unique
    private void epicfighttinkercompat$putToBox() {
        if (containingSkill != null) {
            SkillToItemDictionary.put(containingSkill);
        }
    }

    @Unique
    private void epicfighttinkercompat$removeFromBox() {
        SkillToItemDictionary.remove();
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void addUpdate(CallbackInfo ci) {
        epicfighttinkercompat$putToBox();
    }

    @Inject(method = "update", at = @At("RETURN"))
    private void removeUpdate(CallbackInfo ci) {
        epicfighttinkercompat$removeFromBox();
    }

    @Inject(method = {"requestCasting", "requestHold"}, at = @At("HEAD"))
    private void addRequest(CallbackInfoReturnable<Boolean> ci) {
        epicfighttinkercompat$putToBox();
    }

    @Inject(method = {"requestCasting", "requestHold"}, at = @At("RETURN"))
    private void removeRequest(CallbackInfoReturnable<Boolean> ci) {
        epicfighttinkercompat$removeFromBox();
    }

}
