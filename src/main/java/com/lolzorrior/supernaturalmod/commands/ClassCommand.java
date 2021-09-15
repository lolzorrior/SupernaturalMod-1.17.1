package com.lolzorrior.supernaturalmod.commands;

import com.lolzorrior.supernaturalmod.capabilities.ISupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClassStorage;
import com.lolzorrior.supernaturalmod.capabilities.supernatural_classes.SupernaturalClassFactory;
import com.lolzorrior.supernaturalmod.util.LazyOptionalUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.*;

public class ClassCommand {
    private static final Logger LOGGER = LogManager.getLogger();


    public ClassCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        register(dispatcher);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("supernatural").then((Commands.literal("class")).then(Commands.literal("get").executes(classGet -> getClassEcho(classGet.getSource())))
                .then((Commands.literal("set")).then(Commands.argument("class", StringArgumentType.string()).suggests((p_138084_, p_138085_) -> {
                    return SharedSuggestionProvider.suggest(SUPERNATURAL_CLASSES_LIST, p_138085_);
                }).executes((p_198496_0_) -> {
                    if (Arrays.asList(SUPERNATURAL_CLASSES_LIST).contains(StringArgumentType.getString(p_198496_0_, "class"))) {
                        setClass(p_198496_0_.getSource(), StringArgumentType.getString(p_198496_0_, "class"));
                        return 0;
                    }
                    return 0;
                })))));
        LOGGER.info("Class Command Registered.");
    }

    private static String getClass(CommandSourceStack source) {
        if (!(source.getEntity() instanceof Player)) {
            throw new NullPointerException("Source is not a player.");
        }
        return source.getEntity().getCapability(SCLASS).orElseThrow(NullPointerException::new).getsClass();
    }

    private static int setClass(CommandSourceStack source, String sclass) {
        if (!(source.getEntity() instanceof Player)) {
            throw new NullPointerException("Source is not a player.");
        }
        source.getEntity().getCapability(SCLASS).orElseThrow(NullPointerException::new).changeSupernaturalClass(sclass);
        String i = getClass(source);
        source.getEntity().sendMessage(new TranslatableComponent("commands.supernaturalmod.class.set", i), source.getEntity().getUUID());
        return 0;
    }

    private static int getClassEcho(CommandSourceStack source) {
        String i = getClass(source);
        source.getEntity().sendMessage(new TranslatableComponent("commands.supernaturalmod.class.get", i), source.getEntity().getUUID());
        return 0;
    }
}
