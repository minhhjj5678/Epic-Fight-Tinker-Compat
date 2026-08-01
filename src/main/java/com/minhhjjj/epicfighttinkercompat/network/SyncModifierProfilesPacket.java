package com.minhhjjj.epicfighttinkercompat.network;

import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfileReloadListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncModifierProfilesPacket {
    private final List<CompoundTag> tags;

    public SyncModifierProfilesPacket(List<CompoundTag> tags) {
        this.tags = tags;
    }

    public SyncModifierProfilesPacket(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        this.tags = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.tags.add(buf.readNbt());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(this.tags.size());
        for (CompoundTag tag : this.tags) {
            buf.writeNbt(tag);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ModifierProfileReloadListener.processServerPacket(this.tags);
        });
        ctx.get().setPacketHandled(true);
    }
}
