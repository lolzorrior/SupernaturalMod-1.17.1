package com.lolzorrior.supernaturalmod;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.items.RangedClassBookContainer;
import com.lolzorrior.supernaturalmod.items.RangedClassBookItem;
import com.lolzorrior.supernaturalmod.items.RangedClassBookScreen;
import com.lolzorrior.supernaturalmod.networking.SupernaturalPacketHandler;
import com.lolzorrior.supernaturalmod.util.HumanClassCondition;
import com.lolzorrior.supernaturalmod.util.HumanSeedsConverterModifier;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
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

import static com.lolzorrior.supernaturalmod.SupernaturalMod.MOD_ID;

@Mod(MOD_ID)
public class SupernaturalMod {
    public static final String MOD_ID = "supernaturalmod";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation SUPER_CLASS = new ResourceLocation(MOD_ID, "superclass");

    private static final DeferredRegister CLASSES = DeferredRegister.create(SupernaturalClass.class, MOD_ID);
    private static final DeferredRegister ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);
    private static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MOD_ID);
    public static final LootItemConditionType SUPERNATURAL_HUMAN_CONDITION = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation("supernaturalmod_class"), new LootItemConditionType(new HumanClassCondition.Serializer()));

    public static final RegistryObject<HumanSeedsConverterModifier.Serializer> HUMAN_SEED = LOOT.register("human_grass", HumanSeedsConverterModifier.Serializer::new);

    public static final RegistryObject HUMAN = CLASSES.register("human", () -> new SupernaturalClass("Human"));
    public static final RegistryObject MONK = CLASSES.register("monk", () -> new SupernaturalClass("Monk"));
    public static final RegistryObject DEMON = CLASSES.register("demon", () -> new SupernaturalClass("Demon"));
    public static final RegistryObject WEREWOLF = CLASSES.register("werewolf", () -> new SupernaturalClass("Werewolf"));
    public static final RegistryObject WITCHHUNTER = CLASSES.register("witch_hunter", () -> new SupernaturalClass("Witch Hunter"));
    public static final RegistryObject MAGE = CLASSES.register("mage", () -> new SupernaturalClass("Mage"));
    public static final RegistryObject WARLOCK = CLASSES.register("warlock", () -> new SupernaturalClass("Warlock"));
    public static final RegistryObject ZOMBIE = CLASSES.register("zombie", () -> new SupernaturalClass("Zombie"));
    public static final RegistryObject KNIGHT = CLASSES.register("knight", () -> new SupernaturalClass("Knight"));
    public static final RegistryObject RANGEDCLASSBOOKITEM = ITEMS.register("ranged_class_book_item", () -> new RangedClassBookItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject RANGEDCLASSBOOKCONTAINER = CONTAINERS.register("ranged_class_book_container", () -> IForgeContainerType.create(((windowId, inv, data) -> new RangedClassBookContainer(windowId, inv))));

    public SupernaturalMod() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(new ForgeEventSubscriber());
        SupernaturalPacketHandler.register();
        CLASSES.makeRegistry(MOD_ID, () -> new RegistryBuilder<>());
        CLASSES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOOT.register(FMLJavaModLoadingContext.get().getModEventBus());
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
        event.enqueueWork(() -> {
            MenuScreens.register(RangedClassBookContainer.TYPE, RangedClassBookScreen::new);
        });
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

