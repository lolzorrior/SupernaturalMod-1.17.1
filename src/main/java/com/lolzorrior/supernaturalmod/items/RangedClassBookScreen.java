package com.lolzorrior.supernaturalmod.items;

import com.google.common.collect.ImmutableList;
import com.lolzorrior.supernaturalmod.ForgeEventSubscriber;
import com.lolzorrior.supernaturalmod.SupernaturalMod;
import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.lolzorrior.supernaturalmod.SupernaturalMod.RANGEDCLASSBOOKITEM;
import static com.lolzorrior.supernaturalmod.SupernaturalMod.SUPER_CLASS;

public class RangedClassBookScreen extends BookViewScreen implements MenuAccess<RangedClassBookContainer> {
    private final RangedClassBookContainer menu;
    private PageButton rangerButton;
    private PageButton whButton;
    private Inventory playerInv;

    public RangedClassBookScreen(RangedClassBookContainer bookMenu, Inventory inv, Component component) {
        this.menu = bookMenu;
        this.playerInv = inv;
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

    protected void createClassChangeButtons() {
        int var1 = (this.width - 192) / 2;
        this.whButton = (PageButton)this.addRenderableWidget(new PageButton(var1 + 116, 130, true, (pb) -> {
            this.selectWitchHunter();
        }, true));
        this.rangerButton =(PageButton)this.addRenderableWidget(new PageButton(var1 + 116, 130, true, (pb) -> {
            this.selectRanger();
        }, true));
    }

    protected void selectWitchHunter() {
        if (playerInv.player.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass() == "Human") {
            playerInv.player.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass("Witch Hunter");
        }
        playerInv.removeItem(playerInv.player.getMainHandItem());
    }

    protected void selectRanger() {
        if (playerInv.player.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass() == "Human") {
            playerInv.player.getCapability(SupernaturalClass.SCLASS).orElseThrow(NullPointerException::new).setSupernaturalClass("Ranger");
        }
        playerInv.removeItem(playerInv.player.getMainHandItem());
    }

    protected void init() {
        this.createClassChangeButtons();
    }

    @Override
    public boolean mouseClicked(double p_98272_, double p_98273_, int p_98274_) {
        if (p_98274_ == 0) {
            Style style = this.getClickedComponentStyleAt(p_98272_, p_98273_);
            if (style != null && this.handleComponentClicked(style)) {
                return true;
            }
        }
        return super.mouseClicked(p_98272_, p_98273_, p_98274_);
    }

    @Override
    public boolean handleComponentClicked(Style p_98293_) {
        ClickEvent var2 = p_98293_.getClickEvent();
        if (var2 == null) {
            return false;
        } else if (var2.getAction() == ClickEvent.Action.RUN_COMMAND) {
            this.closeScreen();
            return true;
        } else {
            boolean var3 = super.handleComponentClicked(p_98293_);
            if (var3 && var2.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.closeScreen();
            }

            return var3;
        }
    }
}

