package com.lolzorrior.supernaturalmod.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SupernaturalUtils {

    private static final Logger LOGGER = LogManager.getLogger();
    private static StackTraceElement[] stacktrace;

    public static boolean checkClientSide (Entity entity) {
        stacktrace = Thread.currentThread().getStackTrace();
        if (entity.level.isClientSide()) {
            LOGGER.info("Entity is on the client side at: " + stacktrace[1].getMethodName());
            return false;
        }
        return true;
    }

    public static boolean checkIsEntityPlayer (Entity entity) {
        stacktrace = Thread.currentThread().getStackTrace();
        if(!(entity instanceof Player)) {
            LOGGER.info("Entity is not a player at: " + stacktrace[1].getMethodName());
            return false;
        }
        return true;
    }

    public static boolean checkClientPlayer (Entity entity) {
        if (checkClientSide(entity) && checkIsEntityPlayer(entity)) {
            return true;
        }
        return false;
    }
}
