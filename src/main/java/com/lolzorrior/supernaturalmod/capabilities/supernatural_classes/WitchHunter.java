package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class WitchHunter extends SupernaturalClass {
    private final String sClass = "Witch Hunter";

    public WitchHunter() {
        sPower = 0;
    }

    public WitchHunter(int i) {
        sPower = i;
    }

    @Override
    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        consumePower(50);
        player.addItem(Items.CROSSBOW.getDefaultInstance());
    }
}
