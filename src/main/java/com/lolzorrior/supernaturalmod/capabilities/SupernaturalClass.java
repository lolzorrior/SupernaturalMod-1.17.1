package com.lolzorrior.supernaturalmod.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class SupernaturalClass extends ForgeRegistryEntry<SupernaturalClass> implements ISupernaturalClass{

    @CapabilityInject(ISupernaturalClass.class)
    public static Capability<ISupernaturalClass> SCLASS = null;

    protected int sPower;
    protected long lastSpell = 0;

    public static String[] SUPERNATURAL_CLASSES_LIST = {
            "Human",
            "Monk",
            "Demon",
            "Werewolf",
            "Witch Hunter",
            "Mage",
            "Warlock",
            "Zombie",
            "Knight",
            "Ranger",
            "Rogue",
            "Apothecary"
    };


    public static void register()
    {
        CapabilityManager.INSTANCE.register(ISupernaturalClass.class);
    }

    @Override
    public SupernaturalClass changeSupernaturalClass(SupernaturalClass classIn, SupernaturalClass newClass) {
        newClass.sPower = classIn.sPower;
        newClass.lastSpell = classIn.lastSpell;
        return newClass;
    }

    public SupernaturalClass getSupernaturalClass() {
        return this;
    }

    public long getLastSpell() {return lastSpell;}

    public void setLastSpell() {lastSpell = System.currentTimeMillis();}

    @Override
    public void consumePower(int points) {
        if (sPower >= points) {
            sPower = sPower - points;
        }
    }

    @Override
    public void fillPower(int points) {
        if ((sPower < 2000) && (sPower + points <= 2000)) {
            sPower = sPower + points;
        } else {
            sPower = 2000;
        }
    }

    @Override
    public void setPower(int points) {
        if (points > 2000) {
            sPower = 2000;
        } else {
            sPower = points;
        }
    }

    @Override
    public int getPower() {
        return sPower;
    }

}

