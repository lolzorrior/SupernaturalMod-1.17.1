package com.lolzorrior.supernaturalmod.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;

import static com.lolzorrior.supernaturalmod.SupernaturalMod.MOD_ID;

public class RangedClassBookItem extends Item {

    private ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/item/ranged_class_book_item.png");

    public RangedClassBookItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand iHand) {
        ItemStack var4 = player.getItemInHand(iHand);
        player.openItemGui(var4, iHand);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(var4, level.isClientSide());
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return new TranslatableComponent("items.supernaturalmod.ranged_book");
    }

}
