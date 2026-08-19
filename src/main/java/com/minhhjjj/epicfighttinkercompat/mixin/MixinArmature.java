package com.minhhjjj.epicfighttinkercompat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.model.Armature;

@Mixin(value = Armature.class, remap = false)
public abstract class MixinArmature {

    @ModifyVariable(
            method = "searchPathIndex(Lyesman/epicfight/api/animation/Joint;Ljava/lang/String;)Lyesman/epicfight/api/animation/Joint$HierarchicalJointAccessor;",
            at = @At("STORE"),
            ordinal = 0)
    private Joint.HierarchicalJointAccessor.Builder modifyNullPathBuilder(Joint.HierarchicalJointAccessor.Builder pathBuilder) {
        if (pathBuilder == null) {
            return Joint.HierarchicalJointAccessor.builder();
        }

        return pathBuilder;
    }
}