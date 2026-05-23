package com.minhhjjj.epicfighttinkercompat.modifiers;

import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class EpicFightModifiers {
    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(EpicFightTinkerCompat.MODID);

    public static final StaticModifier<EpicFightPartStatModifier> EPIC_PART_STATS_CALCULATOR = MODIFIERS.register("epic_part_stats_calculator", EpicFightPartStatModifier::new);

    public static final ModifierId BLOCKING = new ModifierId(TConstruct.MOD_ID, "blocking");
    public static final ModifierId THROWING = new ModifierId(TConstruct.MOD_ID, "throwing");

    // WOM
    public static final ModifierId TORMENT_BLADE = new ModifierId(EpicFightTinkerCompat.MODID,"torment_blade");
    public static final ModifierId RUINE_BLADE = new ModifierId(EpicFightTinkerCompat.MODID,"ruine_blade");
    public static final ModifierId AGONY_SPEAR = new ModifierId(EpicFightTinkerCompat.MODID, "agony_spear");
}