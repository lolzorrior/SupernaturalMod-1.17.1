package com.lolzorrior.supernaturalmod.networking;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.mojang.math.Vector3d;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

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
            if (ctx.get().getDirection().getOriginationSide().isServer()) {
                return;
            }
            // do stuff
            if (!(sender.level.isClientSide())) {
                switch (sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()) {
                    case "Werewolf": {
                        if (sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() >= 50) {
                            sender.sendMessage(new TextComponent("The hunt is on!"), sender.getUUID());
                            sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).consumePower(50);
                            sender.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3600));
                            sender.sendMessage(new TextComponent("You have " + sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() + " power left."), sender.getUUID());
                        } else {
                            sender.sendMessage(new TextComponent("Not enough power."), sender.getUUID());
                        }
                        break;
                    }
                    case "Monk": {
                        if (sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() >= 50) {
                            sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).consumePower(50);
                            sender.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 3));
                            sender.sendMessage(new TextComponent("You have " + sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() + " power left."), sender.getUUID());
                        } else {
                            sender.sendMessage(new TextComponent("Not enough power."), sender.getUUID());
                        }
                        break;
                    }
                    case "Witch Hunter": {
                        if (sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() >= 50) {
                            sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).consumePower(50);
                            sender.addItem(Items.CROSSBOW.getDefaultInstance());
                            sender.sendMessage(new TextComponent("You have " + sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() + " power left."), sender.getUUID());
                        } else {
                            sender.sendMessage(new TextComponent("Not enough power."), sender.getUUID());
                        }
                        break;
                    }
                    case "Zombie": {
                        if (sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() >= 50) {
                            sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).consumePower(50);
                            sender.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 3));
                            sender.sendMessage(new TextComponent("You have " + sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() + " power left."), sender.getUUID());
                        } else {
                            sender.sendMessage(new TextComponent("Not enough power."), sender.getUUID());
                        }
                        break;
                    }
                    case "Demon": {
                        if (sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() >= 50) {
                            sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).consumePower(50);
                            sender.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 3));
                            sender.sendMessage(new TextComponent("You have " + sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() + " power left."), sender.getUUID());
                        } else {
                            sender.sendMessage(new TextComponent("Not enough power."), sender.getUUID());
                        }
                        break;
                    }
                    case "Warlock": {
                        if (sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() >= 50) {
                            sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).consumePower(50);
                            sender.addEffect(new MobEffectInstance(MobEffects.HEAL, 0, 1));
                            sender.sendMessage(new TextComponent("You have " + sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() + " power left."), sender.getUUID());
                        } else {
                            sender.sendMessage(new TextComponent("Not enough power."), sender.getUUID());
                        }
                        break;
                    }
                    case "Mage": {
                        if (sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() >= 50) {
                            sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).consumePower(50);

                            Vec3 vector3d = sender.getViewVector(1.0F);
                            double d2 = sender.getX() - (sender.getX() + vector3d.x * 4.0D);
                            double d3 = sender.getY(0.5D) - (0.5D + sender.getY(0.5D));
                            double d4 = sender.getZ() - (sender.getZ() + vector3d.z * 4.0D);

                            LargeFireball fireballentity = new LargeFireball(sender.level, sender, d2, d3, d4, 1);
                            fireballentity.setPos(sender.getX() + vector3d.x * 4.0D, sender.getY(0.5D) + 0.5D, fireballentity.getZ() + vector3d.z * 4.0D);
                            sender.level.addFreshEntity(fireballentity);

                            sender.sendMessage(new TextComponent("You have " + sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() + " power left."), sender.getUUID());
                        } else {
                            sender.sendMessage(new TextComponent("Not enough power."), sender.getUUID());
                        }
                        break;
                    }
                    case "Human": {
                        if (sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() >= 50) {
                            sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).consumePower(50);
                            sender.addEffect(new MobEffectInstance(MobEffects.LUCK, 200, 1));
                            sender.sendMessage(new TextComponent("You have " + sender.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower() + " power left."), sender.getUUID());
                        } else {
                            sender.sendMessage(new TextComponent("Not enough power."), sender.getUUID());
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
