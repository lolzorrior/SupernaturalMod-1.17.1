package com.lolzorrior.supernaturalmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;
import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SUPERNATURAL_CLASSES;

public class ClassCommand {
    private static final Logger LOGGER = LogManager.getLogger();


    public ClassCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        register(dispatcher);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("supernatural").then((Commands.literal("class")).executes((classGet -> getClassEcho(classGet.getSource())))
                .then(Commands.argument("class", StringArgumentType.string())).executes((p_198496_0_) -> {
                    if (Arrays.asList(SUPERNATURAL_CLASSES).contains(StringArgumentType.getString(p_198496_0_, "class"))) {
                        setClass(p_198496_0_.getSource(), StringArgumentType.getString(p_198496_0_, "class"));
                        return 0;
                    }
                    return 0;
                })));
        LOGGER.info("Power Command Registered.");
    }

    private static String getClass(CommandSourceStack source) {
        if (!(source.getEntity() instanceof Player)) {
            throw new NullPointerException("Source is not a player.");
        }
        return source.getEntity().getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass();
    }

    private static int setClass(CommandSourceStack source, String sclass) {
        if (!(source.getEntity() instanceof Player)) {
            throw new NullPointerException("Source is not a player.");
        }
        source.getEntity().getCapability(SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass(sclass);
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
