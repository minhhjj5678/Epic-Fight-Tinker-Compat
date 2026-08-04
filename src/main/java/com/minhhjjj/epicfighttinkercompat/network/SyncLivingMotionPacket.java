package com.minhhjjj.epicfighttinkercompat.network;

import com.minhhjjj.epicfighttinkercompat.events.ShootingAnimationPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.function.Supplier;

public class SyncLivingMotionPacket {
    public SyncLivingMotionPacket() {}

    public SyncLivingMotionPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerPatch playerPatch = EpicFightCapabilities.getServerPlayerPatch(ctx.get().getSender());
            if (playerPatch != null) {
                playerPatch.modifyLivingMotionByCurrentItem();
                ShootingAnimationPlayer.removeUpdateMotionFlag(ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
