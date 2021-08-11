package com.lolzorrior.supernaturalmod.items;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class RangedClassBookScreen extends BookViewScreen implements MenuAccess<RangedClassBookContainer> {
    private final RangedClassBookContainer menu;

    public RangedClassBookScreen(RangedClassBookContainer bookMenu, Inventory inv, Component component) {
        this.menu = bookMenu;
    }

    static List<String> loadPages(CompoundTag p_169695_) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        loadPages(p_169695_, builder::add);
        return builder.build();
    }

    @Override
    public RangedClassBookContainer getMenu() {
        return this.menu;
    }


    @OnlyIn(Dist.CLIENT)
    public static class RangedClassBookAccess implements BookViewScreen.BookAccess {
        private final List<String> pages;

        public RangedClassBookAccess(ItemStack p_98314_) {
            this.pages = readPages(p_98314_);
        }

        private static List<String> readPages(ItemStack p_98319_) {
            CompoundTag compoundtag = p_98319_.getTag();
            return (List<String>)(compoundtag != null ? RangedClassBookScreen.loadPages(compoundtag) : ImmutableList.of());
        }

        public int getPageCount() {
            return this.pages.size();
        }

        public FormattedText getPageRaw(int p_98317_) {
            return FormattedText.of(this.pages.get(p_98317_));
        }
    }

}

