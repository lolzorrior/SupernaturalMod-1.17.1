package com.lolzorrior.supernaturalmod.networking;

import com.lolzorrior.supernaturalmod.capabilities.ISupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.supernatural_classes.SupernaturalClassFactory;
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
        int stringLength = msg.sClass.length();
        buf.writeInt(stringLength);
        buf.writeInt(msg.power);
        buf.writeCharSequence(msg.sClass, StandardCharsets.UTF_8);

        LOGGER.info("Encoding Power: " + msg.power);
    }

    public static PowerUpdatePacket decode(ByteBuf buf) {
        int sLength = buf.readInt();
        int iPower = buf.readInt();
        String sClass = (String) buf.readCharSequence(sLength, StandardCharsets.UTF_8);
        LOGGER.info("Decoding Power");
        return new PowerUpdatePacket(iPower, sClass);
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
            SupernaturalClass sclass = sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass();
            String stringClass = sclass.getsClass();
            if (stringClass.equals("Human")) {
                sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).changeSupernaturalClass(stringClass);
                sender.sendMessage(new TextComponent("Your class is now: " + sclass.getsClass()), sender.getUUID());
            }
            else if (stringClass.equals(msg.sClass)) {
                sclass.fillPower(msg.power);
            }
            sender.sendMessage(new TextComponent("Your power is now: " + sclass.getPower()),sender.getUUID());
            LOGGER.info("Power Updated: " + sclass.getPower());
        });
        ctx.get().setPacketHandled(true);
    }
}
