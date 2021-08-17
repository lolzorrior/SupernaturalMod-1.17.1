package com.lolzorrior.supernaturalmod.capabilities;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class SupernaturalClass extends ForgeRegistryEntry<SupernaturalClass> implements ISupernaturalClass{

    @CapabilityInject(ISupernaturalClass.class)
    public static Capability<ISupernaturalClass> SCLASS = null;

    protected String sClass;
    protected int sPower;
    protected long lastSpell;

    public static String[] SUPERNATURAL_CLASSES_LIST = {
            "Human",
            "Monk",
            "Demon",
            "Werewolf",
            "Witch Hunter",
            "Mage",
            "Warlock",
            "Zombie",
            "Knight",
            "Ranger",
            "Rogue",
            "Apothecary"
    };


    public static void register()
    {
        CapabilityManager.INSTANCE.register(ISupernaturalClass.class);
    }

    public static void encode(SupernaturalClass msg, ByteBuf buf){
        int sClassLength = buf.writeCharSequence(msg.sClass, StandardCharsets.UTF_8);
        buf.writeInt(sClassLength);
        buf.writeInt(msg.sPower);
    }

    public static SupernaturalClass decode(ByteBuf buf){
        return new SupernaturalClass((String) (buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8)), buf.readInt());
    }

    public static void handle(SupernaturalClass msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be threadsafe (most work)
            Player sender = ctx.get().getSender(); // the client that sent this packet
            // do stuff
        });
        ctx.get().setPacketHandled(true);
    }

    public void setSupernaturalClass(String classes) {
        sClass = classes;
    }

    public String getSupernaturalClass() {
        return sClass;
    }

    public long getLastSpell() {return  lastSpell;}

    public void setLastSpell() {lastSpell = System.currentTimeMillis();}

    @Override
    public void consumePower(int points) {
        if (sPower >= points) {
            sPower = sPower - points;
        }
    }

    @Override
    public void fillPower(int points) {
        if ((sPower < 2000) && (sPower + points <= 2000)) {
            sPower = sPower + points;
        } else {
            sPower = 2000;
        }
    }

    @Override
    public void setPower(int points) {
        if (points > 2000) {
            sPower = 2000;
        } else {
            sPower = points;
        }
    }

    @Override
    public int getPower() {
        return sPower;
    }

    public SupernaturalClass() {
        setSupernaturalClass("Human");
        setPower(0);
    }

    public SupernaturalClass(String isClass) {
        setSupernaturalClass(isClass);
        setPower(0);
    }

    public SupernaturalClass(String isClass, int isPower) {
        setSupernaturalClass(isClass);
        setPower(isPower);
    }

}

 class SupernaturalFactory implements Callable<ISupernaturalClass> {
    public ISupernaturalClass call() throws Exception {
        return new SupernaturalClass();
    }
}
