package com.lolzorrior.supernaturalmod.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.lolzorrior.supernaturalmod.SupernaturalMod.MOD_ID;

public class PowerCrystalItem extends Item {

    private ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/item/supernatural_crystal.png");

    public PowerCrystalItem(Properties p_41383_) {
        super(p_41383_);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand iHand) {
     ItemStack item = player.getItemInHand(iHand);
     item.shrink(1);
     return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return new TranslatableComponent("items.supernaturalmod.supernatural_crystal");
    }
}
