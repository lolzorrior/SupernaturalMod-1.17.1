package com.lolzorrior.supernaturalmod.networking;

import com.lolzorrior.supernaturalmod.SupernaturalMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import java.util.function.Supplier;

public class InventoryPacket {

    private static int slot;
    public InventoryPacket() {
        slot = -1;
    }

    public InventoryPacket(int islot) {
        slot = islot;
    }

    public static void encode(InventoryPacket msg, ByteBuf buf) {
        buf.writeInt(slot);
    }

    public static InventoryPacket decode(ByteBuf buf) {
        return new InventoryPacket(buf.readInt());
    }

    public static void handle(InventoryPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be threadsafe (most work)
            Player sender = ctx.get().getSender(); // the client that sent this packet
            if (ctx.get().getDirection().getOriginationSide().isServer()) {
                return;
            }
            sender.getInventory().removeItem(sender.getMainHandItem());


        });
    }
}
