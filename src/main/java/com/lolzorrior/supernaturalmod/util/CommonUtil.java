package com.lolzorrior.supernaturalmod.util;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

public class CommonUtil {
    public static void PowerUpdateMessage(Entity entityIn) {
        entityIn.sendMessage(new TranslatableComponent("power.supernaturalmod.update", entityIn.getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower()), entityIn.getUUID());
    }
}
