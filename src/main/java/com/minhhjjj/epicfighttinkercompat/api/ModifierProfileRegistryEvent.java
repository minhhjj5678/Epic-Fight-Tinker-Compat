package com.minhhjjj.epicfighttinkercompat.api;

import net.minecraftforge.eventbus.api.Event;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfile;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.IModBusEvent;

public class ModifierProfileRegistryEvent extends Event implements IModBusEvent {
    private final Map<ResourceLocation, ModifierProfile> registry;

    public ModifierProfileRegistryEvent(Map<ResourceLocation, ModifierProfile> registry) {
        this.registry = registry;
    }

    public void register(ModifierProfile profile) {
        if (profile != null) {
            this.registry.put(profile.modifierId(), profile);
        }
    }
}