package com.lolzorrior.supernaturalmod.networking;

import com.lolzorrior.supernaturalmod.capabilities.ISupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class PowerUpdatePacket {
    private static final Logger LOGGER = LogManager.getLogger();
    int power = 0;
    String sClass = "";

    public PowerUpdatePacket(){}

    public PowerUpdatePacket(int input, String classInput) {
        power = input;
        sClass = classInput;
    }

    public static void encode(PowerUpdatePacket msg, ByteBuf buf) {
        buf.writeInt(msg.power);
        int isClass = buf.writeCharSequence(msg.sClass, StandardCharsets.UTF_8);
        buf.writeInt(isClass);
        LOGGER.info("Encoding Power: " + msg.power);
    }

    public static PowerUpdatePacket decode(ByteBuf buf) {
        LOGGER.info("Decoding Power");
        return new PowerUpdatePacket(buf.readInt(), (String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8));
    }

    public static void handle(PowerUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be threadsafe (most work)
            LogicalSide sideRecieved = ctx.get().getDirection().getReceptionSide();
            Player sender = ctx.get().getSender(); // the client that sent this packet
            // do stuff
            if (sideRecieved != LogicalSide.SERVER) {
                LOGGER.info("This isn't the server!");
                return;
            }
            ISupernaturalClass sclass = sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new);
            String stringClass = sclass.getSupernaturalClass();
            if (stringClass.equals("Human")) {
                sclass.setSupernaturalClass(msg.sClass);
                sender.sendMessage(new TextComponent("Your class is now: " + sclass.getSupernaturalClass()), sender.getUUID());
            }
            else if (stringClass.equals(msg.sClass)) {
                sclass.fillPower(msg.power);
                sender.sendMessage(new TextComponent("Your power is now: " + sclass.getPower()),sender.getUUID());
            }
            LOGGER.info("Power Updated: " + sclass.getPower());
        });
        ctx.get().setPacketHandled(true);
    }
}
