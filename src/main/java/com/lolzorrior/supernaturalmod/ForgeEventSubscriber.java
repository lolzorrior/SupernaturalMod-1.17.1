package com.lolzorrior.supernaturalmod;

import com.lolzorrior.supernaturalmod.capabilities.ISupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClassProvider;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClassStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.CapabilityManager;
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
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = SupernaturalMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ISupernaturalClass.class, new SupernaturalClassStorage(), SupernaturalClass::new);
    }

    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event)
    {
        new PowerCommand(event.getDispatcher());
        new ClassCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if ((event.getObject().world.isRemote())) {return;}
        if (!(event.getObject() instanceof Player)) {return;}
        Player player = (Player) event.getObject();
        if (!(player.getCapability(SupernaturalClassProvider.SUPERNATURAL_CLASS).isPresent())){
            event.addCapability(SupernaturalMod.SUPER_CLASS, new SupernaturalClassProvider());
        }
        LOGGER.info("Capabilities attached");
    }


    @SubscribeEvent
    public static void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();

        LazyOptional<ISupernaturalClass> sclassCapability = player.getCapability(SUPERNATURAL_CLASS);
        ISupernaturalClass supernaturalClass = sclassCapability.orElseThrow(NullPointerException::new);

        player.sendMessage(new TextComponent("Welcome, your power is " + (supernaturalClass.getPower())), event.getEntity().getUUID());
        player.sendMessage(new TextComponent("Your class is " + supernaturalClass.getSupernaturalClass()), event.getEntity().getUUID());
    }


    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity player = event.getPlayer();
        String oSupernaturalClass = event.getOriginal().getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass();
        int oPower = event.getOriginal().getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower();
        player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(oSupernaturalClass);
        player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).set(oPower);
    }

    @SubscribeEvent
    public void onPlayerRespawns(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        LazyOptional<ISupernaturalClass> sclassCapability = player.getCapability(SUPERNATURAL_CLASS);
        ISupernaturalClass supernaturalClass = sclassCapability.orElseThrow(NullPointerException::new);
        String oClass = supernaturalClass.getSupernaturalClass();
        int oPower = supernaturalClass.getPower();

        player.sendMessage(new StringTextComponent("You are a " + oClass + " with " + oPower + " power."), player.getUniqueID());
    }

    @SubscribeEvent
    public void onPlayerEatsFlesh(LivingEntityUseItemEvent.Finish event) {

        if (!event.getEntityLiving().world.isRemote()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }
        if (!(event.getItem().getItem() == Items.ROTTEN_FLESH)) {
            return;
        }
        int powerToAdd = 50;
        String setClass = "Zombie";
        supernaturalPacketHndler.channel.sendToServer(new PowerUpdatePacket(powerToAdd, setClass));
    }


    @SubscribeEvent
    public void onPlayerKillsMob(LivingDeathEvent event) {
        if (event.getEntity().getEntityWorld().isRemote()) {
            return;
        }
        if (!(event.getEntityLiving().getAttackingEntity() instanceof PlayerEntity)) {
            return;
        }
        LivingEntity player = event.getEntityLiving().getAttackingEntity();
        int powerToAdd = 50;
        if (!(event.getEntityLiving().getLastDamageSource().isProjectile())) {
            return;
        }
        if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Witch Hunter")) {
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
            player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
            return;
        }
        else if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            String setClass = "Witch Hunter";
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
            player.sendMessage(new StringTextComponent("Your class is " + player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUniqueID());
        }
    }

    @SubscribeEvent
    public void onPlayerClicksWithArrow(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer().getHeldItemMainhand().getItem() == Items.ARROW)) {
            return;
        }
        if (!(event.getResult() == Event.Result.DEFAULT )) {
            event.getPlayer().sendMessage(new StringTextComponent("Event result is " + event.getResult().toString()), event.getPlayer().getUniqueID());
            return;
        }
        PlayerEntity player = event.getPlayer();
        String setClass = "Human";
        player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
        player.sendMessage(new StringTextComponent("Your class is " + player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getPlayer().getUniqueID());
    }

    @SubscribeEvent
    public void onPlayerBecomesDemon(LivingHurtEvent event) {
        if (event.getEntity().getEntityWorld().isRemote()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof  PlayerEntity)) {
            return;
        }
        if (!(event.getSource().isFireDamage())) {
            return;
        }
        LivingEntity player = event.getEntityLiving();
        if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Demon")) {
            event.setCanceled(true);
            return;
        }
        if (!(event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.CHEST).isItemEqual(new ItemStack(Items.LEATHER_CHESTPLATE)))) {
            return;
        }
        else if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            int powerToAdd = 50;
            String setClass = "Demon";
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
            player.sendMessage(new StringTextComponent("Your class is " + player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUniqueID());
        }
    }

    @SubscribeEvent
    public void onPlayerBecomesWerewolf(LivingHurtEvent event) {
        if (event.getEntity().getEntityWorld().isRemote()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof  PlayerEntity)) {
            return;
        }
        if (!(event.getSource().getImmediateSource() instanceof WolfEntity)) {
            return;
        }
        LivingEntity player = event.getEntityLiving();
        if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Werewolf")) {
            event.setCanceled(true);
            return;
        }
        else if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            int powerToAdd = 50;
            String setClass = "Werewolf";
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
            player.sendMessage(new StringTextComponent("Your class is " + player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUniqueID());
        }
    }

    @SubscribeEvent
    public void onPlayerBecomesMage(PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isRemote) {
            return;
        }
        PlayerEntity player = event.getPlayer();
        int powerToAdd = 50;
        if (event.getPlayer().getHeldItemMainhand().getItem() == Items.LAPIS_BLOCK) {
            if (event.getSide() == LogicalSide.SERVER) {
                if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Mage")) {
                    player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
                    player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
                    event.getPlayer().inventory.decrStackSize(event.getPlayer().inventory.currentItem, 1);
                } else if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
                    String setClass = "Mage";
                    player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
                    player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
                    player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
                    player.sendMessage(new StringTextComponent("Your class is " + player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUniqueID());
                    event.getPlayer().inventory.decrStackSize(event.getPlayer().inventory.currentItem, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerBecomesWarlock(PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isRemote()) {
            return;
        }
        PlayerEntity player = event.getPlayer();
        int powerToAdd = 50;
        if (event.getPlayer().getHeldItemMainhand().getItem() == Items.REDSTONE_BLOCK) {
            if (event.getSide() == LogicalSide.SERVER) {
                if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Warlock")) {
                    player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
                    player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
                    event.getPlayer().inventory.decrStackSize(event.getPlayer().inventory.currentItem, 1);
                } else if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
                    String setClass = "Warlock";
                    player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
                    player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
                    player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
                    player.sendMessage(new StringTextComponent("Your class is " + player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUniqueID());
                    event.getPlayer().inventory.decrStackSize(event.getPlayer().inventory.currentItem, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerBecomesMonk(LivingDeathEvent event) {
        if (!(event.getEntityLiving().getAttackingEntity() instanceof PlayerEntity)) {
            return;
        }
        LivingEntity player = event.getEntityLiving().getAttackingEntity();
        int powerToAdd = 50;
        if (!(player.getHeldItemMainhand().isEmpty())) {
            player.sendMessage(new StringTextComponent("Hand isn't empty."), event.getEntity().getUniqueID());
            return;
        }
        if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Monk")) {
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
            player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
            return;
        } else if (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Human")) {
            String setClass = "Monk";
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(powerToAdd);
            player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(setClass);
            player.sendMessage(new StringTextComponent("Updated Power: " + (player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower())), event.getEntity().getUniqueID());
            player.sendMessage(new StringTextComponent("Your class is " + player.getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass()), event.getEntity().getUniqueID());
        }
    }

    @SubscribeEvent
    public void wolfTargetsWerewolf(LivingAttackEvent event) {
        if (event.getEntity().getEntityWorld().isRemote()) {
            return;
        };
        if (!(event.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }
        if (!(event.getEntityLiving().getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Werewolf"))) {
            return;
        }
        if (!(event.getSource().getImmediateSource() instanceof WolfEntity)) {
            return;
        }
        ((WolfEntity) event.getEntity()).setAttackTarget(null);
        ((WolfEntity) event.getEntity()).setAggroed(false);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void wolfTargetingEvent(LivingSetAttackTargetEvent event) {
        if (!(event.getEntity() instanceof WolfEntity)) {
            return;
        }
        if (!(event.getTarget() instanceof PlayerEntity)) {
            return;
        }
        if (!(event.getTarget().getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Werewolf"))) {
            return;
        }
        ((WolfEntity) event.getEntity()).setAttackTarget(null);
        ((WolfEntity) event.getEntity()).setAggroed(false);
    }

    @SubscribeEvent
    public void undeadTargetingEvent(LivingSetAttackTargetEvent event) {
        if (!(event.getEntity() instanceof ZombieEntity)) {
            return;
        }
        if (!(event.getTarget() instanceof PlayerEntity)) {
            return;
        }
        if (!(event.getTarget().getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Zombie"))) {
            return;
        }
        ((ZombieEntity) event.getEntity()).setAttackTarget(null);
    }

    @SubscribeEvent
    public void wolfHunt(PlayerInteractEvent.RightClickEmpty event) {
        if (!event.getPlayer().getEntityWorld().isRemote()) {
            return;
        }
        if (!event.getPlayer().getHeldItemMainhand().isEmpty()) {
            return;
        }
        if (event.getHand() != Hand.MAIN_HAND) {
            return;
        }
        supernaturalPacketHndler.channel.sendToServer(new PowerUsePacket("CastBasic"));
    }

    @SubscribeEvent
    public void consume(LivingDeathEvent event) {
        LivingEntity creature = event.getEntityLiving();
        if (!(creature.getAttackingEntity() instanceof PlayerEntity)) {
            return;
        }
        if (!(creature instanceof CreatureEntity)) {
            return;
        }
        if (!(creature.getAttackingEntity().getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getSupernaturalClass().equals("Werewolf"))) {
            return;
        }
        creature.getAttackingEntity().getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).fill(50);
        creature.getAttackingEntity().sendMessage(new StringTextComponent("You take a bite from the creatures corpse. Updated power: " + creature.getAttackingEntity().getCapability(SUPERNATURAL_CLASS).orElseThrow(NullPointerException::new).getPower()), creature.getAttackingEntity().getUniqueID());
    }

}
