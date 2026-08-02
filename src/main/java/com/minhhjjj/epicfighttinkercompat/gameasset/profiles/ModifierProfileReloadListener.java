package com.minhhjjj.epicfighttinkercompat.gameasset.profiles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.api.ModifierProfileRegistryEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModifierProfileReloadListener extends SimpleJsonResourceReloadListener {
    public static final String DIRECTORY = "modifier_profiles";
    private static final Gson GSON = (new GsonBuilder()).create();

    private static final Map<ResourceLocation, ModifierProfile> MODIFIER_PROFILE_MAP = new ConcurrentHashMap<>();
    private static final List<CompoundTag> RAW_COMPOUND_TAGS = new ArrayList<>();

    public ModifierProfileReloadListener() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> jsonMap, @NotNull ResourceManager resource, @NotNull ProfilerFiller filler) {
        reset();

        ModifierProfileRegistryEvent event = new ModifierProfileRegistryEvent(MODIFIER_PROFILE_MAP);
        ModLoader.get().postEvent(event);

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
            try {
                CompoundTag ctag;
                ctag = TagParser.parseTag(entry.getValue().toString());
                ModifierProfile modifierProfile = deserialize(entry.getKey(), ctag);
                MODIFIER_PROFILE_MAP.put(modifierProfile.modifierId(), modifierProfile);
                RAW_COMPOUND_TAGS.add(ctag);
            } catch (Exception e) {
                EpicFightTinkerCompat.LOGGER.error("Failed to parse modifier profile in {}", entry.getKey(), e);
            }
        }
        EpicFightTinkerCompat.LOGGER.info("Loaded {} modifier profiles", MODIFIER_PROFILE_MAP.size());
    }

    public static ModifierProfile get(ResourceLocation id) {
        return MODIFIER_PROFILE_MAP.get(id);
    }

    public static ModifierProfile deserialize(ResourceLocation rl, CompoundTag tag) {
        if (!tag.contains("modifier_id")) {
            throw new IllegalArgumentException("Missing ModifierProfile id for modifier profile in " + rl.toString());
        }

        ModifierId id = new ModifierId(tag.getString("modifier_id"));
        ModifierProfile.Builder builder = ModifierProfile.builder(id);
        if (tag.contains("priority", Tag.TAG_INT)) {
            builder.priority(tag.getInt("priority"));
        }

        if (tag.contains("weapon_type", Tag.TAG_STRING)) {
            ResourceLocation weaponType = ResourceLocation.tryParse(tag.getString("weapon_type"));
            if (weaponType != null) {
                builder.type(weaponType);
                if (!ModList.get().isLoaded(weaponType.getNamespace())) {
                    EpicFightTinkerCompat.LOGGER.warn("Modifier profile '{}' references weapon type '{}', but the mod '{}' is not loaded.", rl, weaponType, weaponType.getNamespace());
                }
            }
        }

        if (tag.contains("weapon_category",  Tag.TAG_STRING)) {
            String categoryName = tag.getString("weapon_category");
            categoryName = categoryName.toUpperCase();
            WeaponCategory category = WeaponCategory.ENUM_MANAGER.get(categoryName);
            if (category != null) {
                builder.weaponCategory(category);
            } else {
                EpicFightTinkerCompat.LOGGER.warn("Unknown weapon category in profile {}: {}", rl, categoryName);
            }
        }

        return builder.build();
    }

    private static void reset() {
        MODIFIER_PROFILE_MAP.clear();
        RAW_COMPOUND_TAGS.clear();
        MODIFIER_PROFILE_MAP.put(ModifierProfiles.ARROW_TEMPEST.modifierId(), ModifierProfiles.ARROW_TEMPEST);
        MODIFIER_PROFILE_MAP.put(ModifierProfiles.EAGLE_EYE.modifierId(), ModifierProfiles.EAGLE_EYE);
    }

    public static List<CompoundTag> getSyncData() {
        return RAW_COMPOUND_TAGS;
    }

    @OnlyIn(Dist.CLIENT)
    public static void processServerPacket(List<CompoundTag> tags) {
        reset();
        ResourceLocation dummy = ResourceLocation.fromNamespaceAndPath("network_sync", "dummy");
        for (CompoundTag tag : tags) {
            try {
                ModifierProfile profile = deserialize(dummy, tag);
                MODIFIER_PROFILE_MAP.put(profile.modifierId(), profile);
            } catch (Exception e) {
                EpicFightTinkerCompat.LOGGER.error("Client failed to parse synced profile from Server", e);
            }
        }
    }
}
