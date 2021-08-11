package com.lolzorrior.supernaturalmod.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ObjectHolder;

import static com.lolzorrior.supernaturalmod.SupernaturalMod.MOD_ID;

public class RangedClassBookContainer extends AbstractContainerMenu {

    private final Player player;
    public final boolean isLocalWorld;

    public RangedClassBookContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, true);
    }

    public RangedClassBookContainer(int id, Inventory playerInventory, boolean localWorld) {
        super(TYPE, id);
        this.isLocalWorld = localWorld;
        this.player = playerInventory.player;
    }


    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }

    @ObjectHolder("supernaturalmod:ranged_class_book_container")
    public static MenuType<RangedClassBookContainer> TYPE;
    public static  final ResourceLocation BOOK_BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/book");
}
