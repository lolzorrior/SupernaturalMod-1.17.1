package com.lolzorrior.supernaturalmod.networking;

import com.lolzorrior.supernaturalmod.capabilities.ISupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.function.Supplier;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

public class PowerUsePacket {
    private static final Logger LOGGER = LogManager.getLogger();

    private int powerUsed;

    public PowerUsePacket(){this.powerUsed = 0;}

    public PowerUsePacket(int powerUsed) {this.powerUsed = powerUsed;}

    public static void encode(PowerUsePacket msg, ByteBuf buf){
        buf.writeInt(msg.powerUsed);
    }

    public static PowerUsePacket decode(ByteBuf buf){
        return new PowerUsePacket(buf.readInt());
    }

    public static void handle(PowerUsePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be threadsafe (most work)
            Player sender = ctx.get().getSender(); // the client that sent this packet
            // do stuff
            ISupernaturalClass capability = sender.getCapability(SCLASS).orElseThrow(NullPointerException::new);
            if (!(sender.level.isClientSide())) {
                if (capability.getLastSpell() + 5000 <= System.currentTimeMillis()) {
                    capability.setLastSpell();
                    switch (msg.powerUsed)
                    {
                        case 1: {
                            if (capability.getPower() < 50) {
                                sender.sendMessage(new TranslatableComponent("message.supernatural.not_enough_power"), sender.getUUID());
                                return;
                            }
                            sender.getCapability(SCLASS).orElseThrow(NullPointerException::new).castLevelOneSpell(sender);
                            CommonUtil.PowerUpdateMessage(sender);
                            break;
                            }
                        case 2: {
                            sender.sendMessage(new TextComponent("Level 2 power used."), sender.getUUID());
                        }
                        default: {
                            break;
                        }
                    }
                } else {
                    int i = (int) ((capability.getLastSpell() + 5000) - System.currentTimeMillis())/1000;
                    sender.sendMessage(new TranslatableComponent("message.supernaturalmod.on_cooldown", i), sender.getUUID());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
