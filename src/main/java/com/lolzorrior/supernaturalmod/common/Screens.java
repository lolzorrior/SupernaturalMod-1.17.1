package com.lolzorrior.supernaturalmod.common;

import com.lolzorrior.supernaturalmod.items.RangedClassBookContainer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;

public class Screens {
    public static void openBookScreen(Player iplayer) {
        iplayer.openMenu(new SimpleMenuProvider(((i, inventory, player) -> new RangedClassBookContainer(i, inventory, player.isLocalPlayer())), new TranslatableComponent("container.supernaturalmod.ranged_class_book")));
    }
}
