package com.lolzorrior.supernaturalmod.capabilities;

import com.lolzorrior.supernaturalmod.capabilities.supernatural_classes.Human;
import com.lolzorrior.supernaturalmod.capabilities.supernatural_classes.SupernaturalClassFactory;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

public class SupernaturalClassStorage implements ICapabilitySerializable<CompoundTag> {

    private static LazyOptional<ISupernaturalClass> holder = LazyOptional.of(Human::new);

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag cTag = new CompoundTag();
        cTag.putString("sClass", holder.orElseThrow(NullPointerException::new).getsClass());
        cTag.putInt("sPower", holder.orElseThrow(NullPointerException::new).getPower());
        return cTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (!(nbt instanceof CompoundTag)) {
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        }
        holder = LazyOptional.of(() -> SupernaturalClassFactory.newSupernaturalClass(nbt.getString("sClass")));
        holder.orElseThrow(NullPointerException::new).setPower(nbt.getInt("sPower"));
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

    public void setCapability(String sClassIn, int powerIn){
        holder = LazyOptional.of(() -> SupernaturalClassFactory.newSupernaturalClass(sClassIn));
        holder.orElseThrow(NullPointerException::new).setPower(powerIn);
    }
}
