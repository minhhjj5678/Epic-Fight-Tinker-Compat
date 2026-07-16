package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.utils.PacketBufferCodec;
import yesman.epicfight.skill.SkillDataKey;

public class EFTSkillDataKeys {
    public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath("epicfight", "skill_data_keys"), EpicFightTinkerCompat.MODID);

    public static final RegistryObject<SkillDataKey<Float>> ARROW_TEMPEST_COOLDOWN;

    static {
        ARROW_TEMPEST_COOLDOWN = DATA_KEYS.register("arrow_tempest_cooldown", () -> SkillDataKey.createSkillDataKey(PacketBufferCodec.FLOAT, ArrowTempestSkill.DEFAULT_COOLDOWN, false, new Class[]{ArrowTempestSkill.class}));
    }
}
