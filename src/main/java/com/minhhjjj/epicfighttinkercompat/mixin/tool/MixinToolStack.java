package com.minhhjjj.epicfighttinkercompat.mixin.tool;

import com.minhhjjj.epicfighttinkercompat.modifiers.EpicFightModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = ToolStack.class, remap = false)
public abstract class MixinToolStack {

    @Redirect(
            method = "rebuildStats",
            at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/nbt/ModifierNBT$Builder;build()Lslimeknights/tconstruct/library/tools/nbt/ModifierNBT;", ordinal = 0)
    )
    private ModifierNBT epicfighttinkercompat$injectEpicStatsAsTrait(ModifierNBT.Builder builder) {
        builder.add(EpicFightModifiers.EPIC_PART_STATS_CALCULATOR.get(), 1);
        return builder.build();
    }
}