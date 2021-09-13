package com.lolzorrior.supernaturalmod.capabilities;

import com.lolzorrior.supernaturalmod.capabilities.supernatural_classes.SupernaturalClassFactory;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

public class SupernaturalClassStorage implements ICapabilitySerializable<IntTag> {

    private LazyOptional<ISupernaturalClass> holder = LazyOptional.empty();

    @Override
    public IntTag serializeNBT() {
        IntTag intTag = IntTag.valueOf(holder.orElseThrow(NullPointerException::new).getPower());
        return intTag;
    }

    @Override
    public void deserializeNBT(IntTag nbt) {
        if (!(nbt instanceof IntTag)) {
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        }
        holder.orElseThrow(NullPointerException::new).setPower(nbt.getAsInt());
    }



    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == SCLASS) {
            return holder.cast();
        } else {
            return LazyOptional.empty();
        }
    }


}
