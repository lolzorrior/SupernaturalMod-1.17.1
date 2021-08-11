package com.lolzorrior.supernaturalmod.networking;

import com.lolzorrior.supernaturalmod.common.Screens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenBookMenuPacket {
    public OpenBookMenuPacket(){}

    public OpenBookMenuPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Screens.openBookScreen(context.get().getSender());
        });
        return true;
    }
}
