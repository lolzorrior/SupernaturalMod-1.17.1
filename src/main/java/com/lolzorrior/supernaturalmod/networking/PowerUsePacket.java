package com.lolzorrior.supernaturalmod.networking;

import com.lolzorrior.supernaturalmod.capabilities.ISupernaturalClass;
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
                            switch (capability.getsClass()) {
                                case "Werewolf": {
                                    sender.sendMessage(new TextComponent("The hunt is on!"), sender.getUUID());
                                    capability.consumePower(50);
                                    sender.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3600));
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Monk": {
                                    capability.consumePower(50);
                                    sender.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 3));
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Witch Hunter": {
                                    capability.consumePower(50);
                                    sender.addItem(Items.CROSSBOW.getDefaultInstance());
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Zombie": {
                                    capability.consumePower(50);
                                    sender.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 3));
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Demon": {
                                    capability.consumePower(50);
                                    sender.level.explode(sender, sender.getX(), sender.getY(), sender.getZ(), 3.0f, Explosion.BlockInteraction.BREAK);
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Warlock": {
                                    capability.consumePower(50);
                                    sender.addEffect(new MobEffectInstance(MobEffects.HEAL, 0, 1));
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Mage": {
                                    capability.consumePower(50);

                                    Vec3 vector3d = sender.getViewVector(1.0F);
                                    double d2 = vector3d.x * 4.0D;
                                    double d3 = vector3d.y * 4.0D;
                                    double d4 = vector3d.z * 4.0D;

                                    LargeFireball fireballentity = new LargeFireball(sender.level, sender, d2, d3, d4, 1);
                                    fireballentity.setPos(sender.getX(), sender.getY(0.5D), fireballentity.getZ());
                                    sender.level.addFreshEntity(fireballentity);

                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Human": {
                                    capability.consumePower(50);
                                    sender.addEffect(new MobEffectInstance(MobEffects.LUCK, 200, 1));
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Knight": {
                                    if (sender.isPassenger()) {
                                        if (sender.getVehicle() instanceof LivingEntity) {
                                            capability.consumePower(50);
                                            ((LivingEntity) sender.getVehicle()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300));
                                            sender.sendMessage(new TranslatableComponent("message.supernatural.mount_speed_used"), sender.getUUID());
                                        } else {
                                            sender.sendMessage(new TextComponent("Mount up Knight"), sender.getUUID());
                                        }
                                    }
                                    break;
                                }
                                case "Ranger": {
                                    capability.consumePower(50);
                                    Wolf wolf = new Wolf(EntityType.WOLF, sender.level);
                                    ctx.get().getSender().server.overworld().addFreshEntity(wolf);
                                    wolf.tame(sender);
                                    wolf.teleportTo(sender.getX(), sender.getY(), sender.getZ());
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Rogue": {
                                    capability.consumePower(50);
                                    ItemStack potion = Items.SPLASH_POTION.getDefaultInstance();
                                    PotionUtils.setPotion(potion, Potions.POISON);
                                    capability.consumePower(50);
                                    sender.addItem(potion);
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                case "Apothecary": {
                                    capability.consumePower(50);
                                    sender.addItem(Items.BREWING_STAND.getDefaultInstance());
                                    sender.sendMessage(new TextComponent("You have " + capability.getPower() + " power left."), sender.getUUID());
                                    break;
                                }
                                default: {
                                    break;
                                }
                            }
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
