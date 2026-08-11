package com.minhhjjj.epicfighttinkercompat.mixin.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.skill.PersistentWeaponInnateSkill;
import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

    @Inject(method = "update", at = @At("HEAD"))
    private void spoofItem(CallbackInfo ci) {
        SkillToItemDictionary.put(containingSkill);
    }

    @Inject(method = "update", at = @At("RETURN"))
    private void removeItem(CallbackInfo ci) {
        SkillToItemDictionary.remove();
    }
}
