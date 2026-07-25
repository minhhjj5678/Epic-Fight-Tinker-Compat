package com.minhhjjj.epicfighttinkercompat.modifiers;

import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;

public class EpicFightModifiers {
    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(EpicFightTinkerCompat.MODID);

    public static final StaticModifier<EpicFightPartStatModifier> EPIC_PART_STATS_CALCULATOR = MODIFIERS.register("epic_part_stats_calculator", EpicFightPartStatModifier::new);

    public static final ModifierId SHORTSWORD = new ModifierId(EpicFightTinkerCompat.MODID, "shortsword");
    public static final ModifierId LONGSWORD = new ModifierId(EpicFightTinkerCompat.MODID, "longsword");
    public static final ModifierId GREATSWORD = new ModifierId(EpicFightTinkerCompat.MODID, "greatsword");
    public static final ModifierId TACHI = new ModifierId(EpicFightTinkerCompat.MODID, "tachi");
    public static final ModifierId UCHIGATANA = new ModifierId(EpicFightTinkerCompat.MODID, "uchigatana");
    public static final ModifierId AXE = new ModifierId(EpicFightTinkerCompat.MODID, "axe");
    public static final ModifierId SPEARY = new ModifierId(EpicFightTinkerCompat.MODID, "speary");
    public static final ModifierId DAGGER = new ModifierId(EpicFightTinkerCompat.MODID, "dagger");

    public static final ModifierId EAGLE_EYE = new ModifierId(EpicFightTinkerCompat.MODID, "eagle_eye");
    public static final ModifierId ARROW_TEMPEST = new ModifierId(EpicFightTinkerCompat.MODID, "arrow_tempest");

    public static final ModifierId BLOCKING = new ModifierId(TConstruct.MOD_ID, "blocking");
    public static final ModifierId THROWING = new ModifierId(TConstruct.MOD_ID, "throwing");

    // WOM
    public static final ModifierId TORMENT_BLADE = new ModifierId(EpicFightTinkerCompat.MODID,"torment_blade");
    public static final ModifierId RUINE_BLADE = new ModifierId(EpicFightTinkerCompat.MODID,"ruine_blade");
    public static final ModifierId AGONY_SPEAR = new ModifierId(EpicFightTinkerCompat.MODID, "agony_spear");
}