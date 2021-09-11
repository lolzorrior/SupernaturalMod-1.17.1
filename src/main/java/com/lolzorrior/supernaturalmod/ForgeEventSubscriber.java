package com.lolzorrior.supernaturalmod;


import com.lolzorrior.supernaturalmod.capabilities.ISupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClassStorage;
import com.lolzorrior.supernaturalmod.commands.ClassCommand;
import com.lolzorrior.supernaturalmod.commands.PowerCommand;
import com.lolzorrior.supernaturalmod.items.RangedClassBookContainer;
import com.lolzorrior.supernaturalmod.items.RangedClassBookItem;
import com.lolzorrior.supernaturalmod.items.RangedClassBookScreen;
import com.lolzorrior.supernaturalmod.networking.OpenBookMenuPacket;
import com.lolzorrior.supernaturalmod.networking.PowerUpdatePacket;
import com.lolzorrior.supernaturalmod.networking.PowerUsePacket;
import com.lolzorrior.supernaturalmod.networking.SupernaturalPacketHandler;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

import java.awt.event.*;
import java.util.EventListener;

import static com.lolzorrior.supernaturalmod.SupernaturalMod.MOD_ID;
import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private long systemTime = System.currentTimeMillis();



    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event)
    {
        new PowerCommand(event.getDispatcher());
        new ClassCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject().level.isClientSide()) {return;}
        if (!(event.getObject() instanceof Player)) {return;}
        Player player = (Player) event.getObject();
        if (!(player.getCapability(SupernaturalClass.SCLASS).isPresent())){
            event.addCapability(SupernaturalMod.SUPER_CLASS, new SupernaturalClassStorage());
        }
        LOGGER.info("Capabilities attached");
    }


    @SubscribeEvent
    public static void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();

        LazyOptional<ISupernaturalClass> sclassCapability = player.getCapability(SCLASS);
        ISupernaturalClass supernaturalClass = sclassCapability.orElseThrow(NullPointerException::new);

        player.sendMessage(new TextComponent("Welcome, your power is " + (supernaturalClass.getPower())), event.getEntity().getUUID());
        player.sendMessage(new TextComponent("Your class is " + supernaturalClass.getSupernaturalClass()), event.getEntity().getUUID());
    }


    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getPlayer();
        String oSupernaturalClass = event.getOriginal().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass();
        int oPower = event.getOriginal().getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower();
        player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(oSupernaturalClass);
        player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setPower(oPower);
    }

    @SubscribeEvent
    public void onPlayerRespawns(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        LazyOptional<ISupernaturalClass> sclassCapability = player.getCapability(SCLASS);
        ISupernaturalClass supernaturalClass = sclassCapability.orElseThrow(NullPointerException::new);
        String oClass = supernaturalClass.getSupernaturalClass();
        int oPower = supernaturalClass.getPower();

        player.sendMessage(new TextComponent("You are a " + oClass + " with " + oPower + " power."), player.getUUID());
    }

    @SubscribeEvent
    public void onPlayerEatsFlesh(LivingEntityUseItemEvent.Finish event) {
        if (!event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }
        if (!(event.getItem().getItem() == Items.ROTTEN_FLESH)) {
            return;
        }
        int powerToAdd = 50;
        String setClass = "Zombie";
        SupernaturalPacketHandler.channel.sendToServer(new PowerUpdatePacket(powerToAdd, setClass));
    }


    @SubscribeEvent
    public void onPlayerKillsMob(LivingDeathEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving().getKillCredit() instanceof Player)) {
            return;
        }
        LivingEntity player = event.getEntityLiving().getKillCredit();
        int powerToAdd = 50;
        if (event.getEntityLiving().getLastDamageSource() == null) {
            return;
        }
        if (!(event.getEntityLiving().getLastDamageSource().isProjectile())) {
            return;
        }
        if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Witch Hunter")) {
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            CommonUtil.PowerUpdateMessage(player);
            return;
        }
        else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            String setClass = "Witch Hunter";
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            CommonUtil.PowerUpdateMessage(player);
            player.sendMessage(new TextComponent("Your class is " + player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUUID());
        }
    }

    @SubscribeEvent
    public void onPlayerClicksWithArrow(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer().getMainHandItem().getItem() == Items.ARROW)) {
            return;
        }
        if (!(event.getResult() == Event.Result.DEFAULT )) {
            event.getPlayer().sendMessage(new TextComponent("Event result is " + event.getResult().toString()), event.getPlayer().getUUID());
            return;
        }
        Player player = event.getPlayer();
        String setClass = "Human";
        player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
        player.sendMessage(new TextComponent("Your class is " + player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getPlayer().getUUID());
    }

    @SubscribeEvent
    public void onPlayerBecomesDemon(LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof  Player)) {
            return;
        }
        if (!(event.getSource().isFire())) {
            return;
        }
        LivingEntity player = event.getEntityLiving();
        if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Demon")) {
            event.setCanceled(true);
            return;
        }
        if (!(((ArmorItem) (player.getItemBySlot(EquipmentSlot.CHEST).getItem())).getMaterial() == ArmorMaterials.LEATHER)) {
            return;
        }
        else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            int powerToAdd = 50;
            String setClass = "Demon";
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            CommonUtil.PowerUpdateMessage(player);
            player.sendMessage(new TextComponent("Your class is " + player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUUID());
        }
    }

    @SubscribeEvent
    public void onPlayerBecomesWerewolf(LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof  Player)) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Wolf)) {
            return;
        }
        LivingEntity player = event.getEntityLiving();
        if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Werewolf")) {
            event.setCanceled(true);
            return;
        }
        else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            int powerToAdd = 50;
            String setClass = "Werewolf";
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            CommonUtil.PowerUpdateMessage(player);
            player.sendMessage(new TextComponent("Your class is " + player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUUID());
        }
    }

    @SubscribeEvent
    public void onPlayerBecomesMage(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        Player player = event.getPlayer();
        int powerToAdd = 50;
        if (event.getPlayer().getMainHandItem().getItem() == Items.LAPIS_BLOCK) {
            if (event.getSide() == LogicalSide.SERVER) {
                if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Mage")) {
                    player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
                    CommonUtil.PowerUpdateMessage(player);
                    event.getPlayer().getInventory().removeItem(event.getItemStack());
                } else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
                    String setClass = "Mage";
                    player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
                    player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
                    CommonUtil.PowerUpdateMessage(player);
                    player.sendMessage(new TextComponent("Your class is " + player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUUID());
                    event.getPlayer().getInventory().removeItem(event.getItemStack());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerBecomesWarlock(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        Player player = event.getPlayer();
        int powerToAdd = 50;
        if (event.getPlayer().getMainHandItem().getItem() == Items.REDSTONE_BLOCK) {
            if (event.getSide() == LogicalSide.SERVER) {
                if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Warlock")) {
                    player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
                    player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
                    event.getPlayer().getInventory().removeItem(event.getItemStack());
                } else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
                    String setClass = "Warlock";
                    player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
                    player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
                    CommonUtil.PowerUpdateMessage(player);
                    player.sendMessage(new TextComponent("Your class is " + player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUUID());
                    event.getPlayer().getInventory().removeItem(event.getItemStack());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerBecomesMonk(LivingDeathEvent event) {
        if (!(event.getEntityLiving().getKillCredit() instanceof Player)) {
            return;
        }
        LivingEntity player = event.getEntityLiving().getKillCredit();
        int powerToAdd = 50;
        if (!(player.getMainHandItem().isEmpty())) {
            return;
        }
        if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Monk")) {
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            CommonUtil.PowerUpdateMessage(player);
            return;
        } else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            String setClass = "Monk";
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            CommonUtil.PowerUpdateMessage(player);
            player.sendMessage(new TextComponent("Your class is " + player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUUID());
        }
    }

    @SubscribeEvent
    public void wolfAttacksWerewolf(LivingAttackEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }
        if (!(event.getEntityLiving().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Werewolf"))) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Wolf)) {
            return;
        }
        event.setCanceled(true);
    }


    @SubscribeEvent
    public void wolfTargetingEvent(LivingSetAttackTargetEvent event) {
        if (!(event.getEntity() instanceof Wolf)) {
            return;
        }
        if (!(event.getTarget() instanceof Player)) {
            return;
        }
        if (!(event.getTarget().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Werewolf"))) {
            return;
        }
        ((Wolf) event.getEntity()).setPersistentAngerTarget(null);
        ((Wolf) event.getEntity()).setTarget(null);
        ((Wolf) event.getEntity()).setRemainingPersistentAngerTime(1);
    }

    @SubscribeEvent
    public void undeadTargetingEvent(LivingSetAttackTargetEvent event) {
        if (!(event.getEntity() instanceof Zombie)) {
            return;
        }
        if (!(event.getTarget() instanceof Player)) {
            return;
        }
        if (!(event.getTarget().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Zombie"))) {
            return;
        }
        ((Zombie) event.getEntity()).setTarget(null);
    }

    @SubscribeEvent
    public void levelOneSpell(PlayerInteractEvent.RightClickEmpty event) {
        if (!event.getEntity().level.isClientSide()) {
            return;
        }
        if (!event.getPlayer().getMainHandItem().isEmpty()) {
            return;
        }
        if (event.getHand().equals(InteractionHand.OFF_HAND)) {
            return;
        }
        if (System.currentTimeMillis() < systemTime + 5000) {
            int i = (int)((systemTime + 5000) - System.currentTimeMillis());
            event.getPlayer().sendMessage(new TranslatableComponent("message.supernatural.on_cooldown", i), event.getPlayer().getUUID());
            return;
        }
        SupernaturalPacketHandler.channel.sendToServer(new PowerUsePacket(1));
    }

    @SubscribeEvent
    public void consume(LivingDeathEvent event) {
        LivingEntity creature = event.getEntityLiving();
        if (!(creature.getKillCredit() instanceof Player)) {
            return;
        }
        if (!(creature instanceof LivingEntity)) {
            return;
        }
        if (!(creature.getKillCredit().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Werewolf"))) {
            return;
        }
        creature.getKillCredit().getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(50);
        creature.getKillCredit().sendMessage(new TextComponent("You take a bite from the creatures corpse. Updated power: " + creature.getKillCredit().getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower()), creature.getKillCredit().getUUID());
    }

    @SubscribeEvent
    public void onHumanMounts(EntityMountEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityMounting() instanceof Player)) {
            return;
        }
        if (event.getEntityMounting().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            String setClass = "Knight";
            int powerToAdd = 50;
            event.getEntityMounting().getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            event.getEntityMounting().getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            CommonUtil.PowerUpdateMessage(event.getEntityMounting());
            event.getEntityMounting().sendMessage(new TextComponent("Your class is " + event.getEntityMounting().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUUID());
        }
    }

    @SubscribeEvent
    public void onRangedBookGui(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() instanceof RangedClassBookItem) {
            SupernaturalPacketHandler.channel.sendToServer(new OpenBookMenuPacket());
        }
    }

    @SubscribeEvent
    public void onPlayerDrinksPotion(EntityItemPickupEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        if (!(event.getItem().getItem().getItem() instanceof PotionItem)) {
            return;
        }
        if (event.getEntityLiving() instanceof Player) {
            if (event.getEntityLiving().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
                event.getEntityLiving().getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(50);
                event.getEntityLiving().getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass("Apothecary");
                CommonUtil.PowerUpdateMessage(event.getEntityLiving());
                event.getEntityLiving().sendMessage(new TextComponent("Your class is " + event.getEntityLiving().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUUID());
            }
        }
    }

    @SubscribeEvent
    public void onRangedDamage(LivingDamageEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        if (!event.getSource().isProjectile()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Player)) {
            return;
        }
        if (!((event.getSource().getEntity()).getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Ranger"))
                || (event.getSource().getEntity().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Witch Hunter"))) {
            return;
        }
        event.setAmount(event.getAmount() + 2.0f);
        event.getSource().getEntity().sendMessage(new TranslatableComponent("ranged.supernaturalmod.damage", event.getAmount()), event.getSource().getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onFistDamage(LivingDamageEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getSource().getEntity();
        if (damager.getMainHandItem().isEmpty()) {
            if (damager.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Monk")) {
                event.setAmount(event.getAmount() + 1.0f);
                damager.sendMessage(new TranslatableComponent("melee.supernaturalmod.damage", event.getAmount()), damager.getUUID());
            }
        }
    }

    @SubscribeEvent
    public static void onWarlockDefeatsEnemy(LivingDeathEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getSource().getEntity();
        if (damager.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Warlock")) {
            damager.heal(1.0f);
            damager.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(50);
            CommonUtil.PowerUpdateMessage(damager);
        }
    }

    @SubscribeEvent
    public static void onMageDefeatsEnemy(LivingDeathEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getSource().getEntity();
        if (damager.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Mage")) {
            damager.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200));
            damager.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(50);
            CommonUtil.PowerUpdateMessage(damager);
        }
    }
}


