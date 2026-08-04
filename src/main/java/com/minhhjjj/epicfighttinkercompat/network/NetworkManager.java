package com.minhhjjj.epicfighttinkercompat.network;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfileReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID)
public class NetworkManager {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(EpicFightTinkerCompat.MODID, "network_sync"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, SyncModifierProfilesPacket.class,
                SyncModifierProfilesPacket::toBytes,
                SyncModifierProfilesPacket::new,
                SyncModifierProfilesPacket::handle
        );
        CHANNEL.registerMessage(id++, SyncLivingMotionPacket.class,
                SyncLivingMotionPacket::toBytes,
                SyncLivingMotionPacket::new,
                SyncLivingMotionPacket::handle
        );
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null && event.getPlayer().getServer() != null && event.getPlayer().getServer().isSingleplayerOwner(event.getPlayer().getGameProfile())) {
            return;
        }

        SyncModifierProfilesPacket packet = new SyncModifierProfilesPacket(ModifierProfileReloadListener.getSyncData());
        if (event.getPlayer() != null) {
            CHANNEL.send(PacketDistributor.PLAYER.with(event::getPlayer), packet);
        } else {
            CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
        }
    }
}
