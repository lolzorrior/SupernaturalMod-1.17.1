package com.lolzorrior.supernaturalmod;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.networking.SupernaturalPacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

import static com.lolzorrior.supernaturalmod.SupernaturalRegistry.SUPERNATURAL_CLASSES;


//private static final DeferredRegister BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
//DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);


@Mod("supernaturalmod")
public class SupernaturalMod {
    public static final String MOD_ID = "supernaturalmod";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation SUPER_CLASS = new ResourceLocation(MOD_ID, "superclass");

    private static final DeferredRegister SUPER_CLASSES_DF = DeferredRegister.create(SupernaturalClass.class, MOD_ID);
    public static final RegistryObject HUMAN = SUPER_CLASSES_DF.register("human", () -> new SupernaturalClass("Human"));
    public static final RegistryObject MONK = SUPER_CLASSES_DF.register("monk", () -> new SupernaturalClass("Monk"));
    public static final RegistryObject DEMON = SUPER_CLASSES_DF.register("demon", () -> new SupernaturalClass("Demon"));
    public static final RegistryObject WEREWOLF = SUPER_CLASSES_DF.register("werewolf", () -> new SupernaturalClass("Werewolf"));
    public static final RegistryObject WITCHHUNTER = SUPER_CLASSES_DF.register("witchhunter", () -> new SupernaturalClass("Witch Hunter"));
    public static final RegistryObject MAGE = SUPER_CLASSES_DF.register("mage", () -> new SupernaturalClass("Mage"));
    public static final RegistryObject WARLOCK = SUPER_CLASSES_DF.register("warlock", () -> new SupernaturalClass("Warlock"));
    public static final RegistryObject ZOMBIE = SUPER_CLASSES_DF.register("zombie", () -> new SupernaturalClass("Zombie"));
    public static final RegistryObject KNIGHT = SUPER_CLASSES_DF.register("knight", () -> new SupernaturalClass("Knight"));

    public SupernaturalMod() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(new ForgeEventSubscriber());
        SupernaturalPacketHandler.register();
        SUPER_CLASSES_DF.makeRegistry(MOD_ID, () -> new RegistryBuilder<>());
        SUPER_CLASSES_DF.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        SupernaturalClass.register();
        LOGGER.info("Capabilities registered");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("supernaturalmod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");

    }


    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }

    }
}

