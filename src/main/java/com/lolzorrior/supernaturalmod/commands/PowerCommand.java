package com.lolzorrior.supernaturalmod.commands;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PowerCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public PowerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {register(dispatcher);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("supernatural").then((Commands.literal("power")).executes((powerGet -> getPowerEcho(powerGet.getSource()))).then(Commands.argument("power", IntegerArgumentType.integer())
                .executes((p_198496_0_) -> setPower(p_198496_0_.getSource(), IntegerArgumentType.getInteger(p_198496_0_, "power"))))));
        LOGGER.info("Power Command Registered.");
    }

    private static int getPower(CommandSourceStack source) {
        if (source.getEntity() instanceof Player) {
            return source.getEntity().getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getPower();
        } else {
            throw new NullPointerException();
        }
    }

    private static int setPower(CommandSourceStack source, int power) {
        if (source.getEntity() instanceof Player) {
            source.getEntity().getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).setPower(power);
        } else {
            throw new NullPointerException();
        }

        int i = getPower(source);
        source.getEntity().sendMessage(new TranslatableComponent("commands.supernaturalmod.power.set", i), source.getEntity().getUUID());
        return i;
    }

    private static int getPowerEcho(CommandSourceStack source) {
        if (!(source.getEntity() instanceof Player)) {
            return -1;
        }

        int i = getPower(source);
        source.getEntity().sendMessage(new TranslatableComponent("commands.supernaturalmod.power.get", i), source.getEntity().getUUID());
        return i;
    }
}
