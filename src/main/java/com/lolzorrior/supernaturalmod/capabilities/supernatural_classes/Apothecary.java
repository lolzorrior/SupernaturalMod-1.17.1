package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class Apothecary extends SupernaturalClass {
    private final String sClass = "Apothecary";

    public Apothecary() {
        sPower = 0;
    }

    public Apothecary(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        consumePower(50);
        player.addItem(Items.BREWING_STAND.getDefaultInstance());
    }
}
