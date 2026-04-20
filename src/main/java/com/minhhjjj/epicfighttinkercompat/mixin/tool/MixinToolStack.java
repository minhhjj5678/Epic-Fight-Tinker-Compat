package com.minhhjjj.epicfighttinkercompat.mixin.tool;

import java.util.Objects;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;

import net.minecraft.nbt.CompoundTag;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = ToolStack.class, remap = false)
public abstract class MixinToolStack {
    private static final ModifierId EPIC_PART_STATS_ID = new ModifierId(EpicFightTinkerCompat.MODID, "epic_part_stats_calculator");

    @Shadow private CompoundTag nbt;
    @Shadow @Nullable private ModifierNBT upgrades;
    @Shadow public abstract ModifierNBT getUpgrades();

    @Inject(method = "rebuildStats", at = @At("HEAD"))
    private void epicfighttinkercompat$ensureEpicPartStatsModifier(CallbackInfo ci) {
        ModifierNBT current = this.getUpgrades();
        boolean changed = false;

        if (current.getLevel(Objects.requireNonNull(EPIC_PART_STATS_ID)) <= 0) {
            current = current.withModifier(Objects.requireNonNull(EPIC_PART_STATS_ID), 1);
            changed = true;
        }

        if (changed) {
            this.upgrades = current;
            this.nbt.put("tic_upgrades", Objects.requireNonNull(current.serializeToNBT()));
        }
    }
}
