package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;

public class Mage extends SupernaturalClass {
    private final String sClass = "Mage";

    public Mage() {
        sPower = 0;
    }

    public Mage(int i) {
        sPower = i;
    }


    @Override
    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        consumePower(50);

        Vec3 vector3d = player.getViewVector(1.0F);
        double d2 = vector3d.x * 4.0D;
        double d3 = vector3d.y * 4.0D;
        double d4 = vector3d.z * 4.0D;

        LargeFireball fireballentity = new LargeFireball(player.level, player, d2, d3, d4, 1);
        fireballentity.setPos(player.getX(), player.getY(0.5D), fireballentity.getZ());
        player.level.addFreshEntity(fireballentity);
    }
}
