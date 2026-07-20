package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import yesman.epicfight.skill.SkillDataKey;

@SuppressWarnings("unused")
public class EFTSkillDataKeys {
    public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath("epicfight", "skill_data_keys"), EpicFightTinkerCompat.MODID);

    static {
    }
}
