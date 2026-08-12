package com.minhhjjj.epicfighttinkercompat.mixin.itemspoof.client;

import com.google.common.collect.ImmutableMap;
import com.minhhjjj.epicfighttinkercompat.skill.SkillToItemDictionary;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.data.reloader.ItemCapabilityReloadListener;
import yesman.epicfight.network.server.SPDatapackSync;

import java.util.Map;

@Mixin(value = ItemCapabilityReloadListener.class, remap = false)
public abstract class MixinItemCapabilityReloadListenerClient {
    @Final
    @Shadow
    private static Map<Item, CompoundTag> WEAPON_COMPOUNDS;

    @Inject(method = "processServerPacket", at = @At("RETURN"))
    private static void initClientDictionary(SPDatapackSync packet, CallbackInfo ci) {
        SkillToItemDictionary.init(ImmutableMap.copyOf(WEAPON_COMPOUNDS), false);
    }

}
