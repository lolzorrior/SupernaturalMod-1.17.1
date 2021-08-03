package com.lolzorrior.supernaturalmod;


import com.lolzorrior.supernaturalmod.capabilities.ISupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClassStorage;
import com.lolzorrior.supernaturalmod.commands.ClassCommand;
import com.lolzorrior.supernaturalmod.commands.PowerCommand;
import com.lolzorrior.supernaturalmod.networking.PowerUpdatePacket;
import com.lolzorrior.supernaturalmod.networking.PowerUsePacket;
import com.lolzorrior.supernaturalmod.networking.SupernaturalPacketHandler;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

@Mod.EventBusSubscriber(modid = SupernaturalMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    

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
        if (!(event.getEntityLiving().getLastDamageSource().isProjectile())) {
            return;
        }
        if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Witch Hunter")) {
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
            return;
        }
        else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            String setClass = "Witch Hunter";
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
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
        if (!(event.getEntityLiving().getItemBySlot(EquipmentSlot.CHEST).equals(new ItemStack(Items.LEATHER_CHESTPLATE)))) {
            return;
        }
        else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            int powerToAdd = 50;
            String setClass = "Demon";
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
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
            player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
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
                    player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
                    event.getPlayer().getInventory().removeItem(event.getItemStack());
                } else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
                    String setClass = "Mage";
                    player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
                    player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
                    player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
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
                    player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
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
            player.sendMessage(new TextComponent("Hand isn't empty."), event.getEntity().getUUID());
            return;
        }
        if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Monk")) {
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
            return;
        } else if (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            String setClass = "Monk";
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).fillPower(powerToAdd);
            player.getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            player.sendMessage(new TextComponent("Updated Power: " + (player.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUUID());
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
    public void wolfHunt(PlayerInteractEvent.RightClickEmpty event) {
        if (!event.getEntity().level.isClientSide()) {
            return;
        }
        if (!event.getPlayer().getMainHandItem().isEmpty()) {
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

}
