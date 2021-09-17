package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class Monk extends SupernaturalClass {
    private final String sClass = "Monk";
    public Monk() {
        sPower = 0;
    }

    public Monk(int i) {
        sPower = 0;
    }

    @Override
    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        consumePower(50);
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 3));
    }
}
