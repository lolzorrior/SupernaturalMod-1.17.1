package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import com.lolzorrior.supernaturalmod.util.CommonUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Knight extends SupernaturalClass {
    private final String sClass = "Knight";

    public Knight() {
        sPower = 0;
    }

    public Knight(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }

    @Override
    public void castLevelOneSpell(Player player) {
        if (player.isPassenger()) {
            if (player.getVehicle() instanceof LivingEntity) {
                consumePower(50);
                ((LivingEntity) player.getVehicle()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600));
                player.sendMessage(new TranslatableComponent("message.supernatural.mount_speed_used"), player.getUUID());
            } else {
                player.sendMessage(new TextComponent("Mount up Knight"), player.getUUID());
            }
        }
    }
}
