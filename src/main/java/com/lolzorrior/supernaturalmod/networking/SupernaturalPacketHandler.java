package com.lolzorrior.supernaturalmod.networking;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import static com.lolzorrior.supernaturalmod.SupernaturalMod.MOD_ID;
public class SupernaturalPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel channel;

    public SupernaturalPacketHandler() {}

    public static void register() {
        channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MOD_ID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals);
        int id = 0;
        channel.registerMessage(id++, PowerUpdatePacket.class, PowerUpdatePacket::encode, PowerUpdatePacket::decode, PowerUpdatePacket::handle);
        channel.registerMessage(id++, PowerUsePacket.class, PowerUsePacket::encode, PowerUsePacket::decode, PowerUsePacket::handle);
        channel.registerMessage(id++, OpenBookMenuPacket.class, OpenBookMenuPacket::encode, OpenBookMenuPacket::new, OpenBookMenuPacket::handle);
        channel.registerMessage(id++, InventoryPacket.class, InventoryPacket::encode, InventoryPacket::decode, InventoryPacket::handle);
    }
}
