package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class Warlock extends SupernaturalClass {
    private final String sClass = "Warlock";

    public Warlock() {
        sPower = 0;
    }

    public Warlock(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        consumePower(50);
        player.addEffect(new MobEffectInstance(MobEffects.HEAL, 0, 1));
    }
}
