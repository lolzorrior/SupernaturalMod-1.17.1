package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class Rogue extends SupernaturalClass {
    private final String sClass = "Rogue";

    public Rogue() {
        sPower = 0;
    }

    public Rogue(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        consumePower(50);
        ItemStack potion = Items.SPLASH_POTION.getDefaultInstance();
        PotionUtils.setPotion(potion, Potions.POISON);
        consumePower(50);
        player.addItem(potion);
    }
}
