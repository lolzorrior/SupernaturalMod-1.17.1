package com.lolzorrior.supernaturalmod.capabilities;

import com.lolzorrior.supernaturalmod.capabilities.supernatural_classes.Human;
import net.minecraft.core.Direction;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

public class SupernaturalClassStorage implements ICapabilitySerializable<IntTag> {
    private int holder = 0;
    private Player player;
    private boolean run = false;


    @Override
    public IntTag serializeNBT() {
        IntTag intTag = IntTag.valueOf(holder);
        return intTag;
    }

    @Override
    public void deserializeNBT(IntTag nbt) {
        if (!(nbt instanceof IntTag)) {
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        }
        holder = nbt.getAsInt();
    }

    public SupernaturalClassStorage(Player playerIn, boolean firstRun) {
        this.player = playerIn;
        this.run = firstRun;
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == SCLASS && cap != null) {
            if (run == true) {
                run = false;
                return LazyOptional.of(() -> new Human()).cast();
            } else if (player.getCapability(SCLASS).isPresent()) {
                return player.getCapability(SCLASS).cast();
            }
        }
        return LazyOptional.empty();
    }

}
