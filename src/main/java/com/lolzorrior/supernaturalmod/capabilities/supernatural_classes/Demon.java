package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;

public class Demon extends SupernaturalClass {
    private final String sClass = "Demon";

    public Demon() {
        sPower = 0;
    }

    public Demon(int i) {
        sPower = i;
    }

    @Override
    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        consumePower(50);
        player.level.explode(player, player.getX(), player.getY(), player.getZ(), 3.0f, Explosion.BlockInteraction.BREAK);
    }
}
