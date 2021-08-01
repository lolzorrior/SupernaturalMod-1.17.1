package com.lolzorrior.supernaturalmod.capabilities;

import io.netty.buffer.
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.PacketDispatcher;

import java.util.function.Supplier;

public class SupernaturalClass implements ISupernaturalClass {
    private String supernaturalClass;
    private int power;

    public static String[] SUPERNATURAL_CLASSES = {
            "Human",
            "Monk",
            "Demon",
            "Werewolf",
            "Witch Hunter",
            "Mage",
            "Warlock",
            "Zombie"
    };

    public SupernaturalClass() {
        this.setSupernaturalClass("Human");
        this.power = 0;
    }

    public SupernaturalClass(String sclass, int power){
        this.setSupernaturalClass(sclass);
        this.set(power);
    }

    @Override
    public void setSupernaturalClass(String classes) {
        this.supernaturalClass = classes;
    }

    @Override
    public String getSupernaturalClass() {
        return supernaturalClass;
    }


    public void consume(int points) {
        this.power -= points;
        if (this.power < 0) this.power = 0;
    }

    @Override
    public void fill(int points) {
        this.power += points;
    }

    public void set(int points) {
        this.power = points;
    }

    public int getPower() {
        return this.power;
    }

    public static void encode(WrappedCompositeByteBuff buf){
        buf.writeString(msg.supernaturalClass);
        buf.writeInt(msg.power);
    }

    public static SupernaturalClass decode(PacketBuffer buf){
        return new SupernaturalClass(buf.readString(), buf.readInt());
    }

    public static void handle(SupernaturalClass msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be threadsafe (most work)
            ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
            // do stuff
        });
        ctx.get().setPacketHandled(true);
    }
}
