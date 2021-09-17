package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class Werewolf extends SupernaturalClass {
    private final String sClass = "Werewolf";

    public Werewolf() {
        sPower = 0;
    }

    public Werewolf(int i) {
        sPower = i;
    }

    @Override
    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        consumePower(50);
        player.sendMessage(new TextComponent("The hunt is on!"), player.getUUID());
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3600));
    }
}
