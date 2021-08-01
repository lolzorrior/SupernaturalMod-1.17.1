package com.lolzorrior.supernaturalmod.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class SupernaturalClassStorage {
    @Nullable
    public CompoundTag writeTag(Capability<ISupernaturalClass> capability, ISupernaturalClass instance, Direction side) {
        final CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("class", instance.getSupernaturalClass());
        compoundTag.putInt("power", instance.getPower());
        return compoundTag;
    }

    public void readTag(Capability<ISupernaturalClass> capability, ISupernaturalClass instance, Direction side, IntTag tag) {
        final CompoundTag compoundTag = new CompoundTag();
        instance.setSupernaturalClass(compoundTag.getString("class"));
        instance.set(compoundTag.getInt("power"));
    }
}
