package com.minhhjjj.epicfighttinkercompat.mixin.itemspoof.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.data.reloader.ItemCapabilityReloadListener;

import java.util.Map;

@Mixin(value = ItemCapabilityReloadListener.class, remap = false)
public abstract class MixinItemCapabilityReloadListener {

    @Final
    @Shadow
    private static Map<Item, CompoundTag> WEAPON_COMPOUNDS;

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("RETURN"))
    private void initServerDictionary(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn, CallbackInfo ci) {
        SkillToItemDictionary.init(ImmutableMap.copyOf(WEAPON_COMPOUNDS), true);
    }
}
