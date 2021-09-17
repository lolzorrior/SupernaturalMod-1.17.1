package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;

public class Ranger extends SupernaturalClass {
    private final String sClass = "Ranger";

    public Ranger() {
        sPower = 0;
    }

    public Ranger(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        consumePower(50);
        Wolf wolf = new Wolf(EntityType.WOLF, player.level);
        player.level.addFreshEntity(wolf);
        wolf.tame(player);
        wolf.teleportTo(player.getX(), player.getY(), player.getZ());
    }
}
