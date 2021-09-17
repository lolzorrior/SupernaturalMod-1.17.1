package com.lolzorrior.supernaturalmod.capabilities;

import com.lolzorrior.supernaturalmod.capabilities.supernatural_classes.SupernaturalClassFactory;
import com.lolzorrior.supernaturalmod.util.LazyOptionalUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
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
    public void changeSupernaturalClass(String sClass) {
        int powerin = LazyOptionalUtil.getStorage().getCapability(SCLASS).orElseThrow(NullPointerException::new).getPower();
        LazyOptionalUtil.getStorage().setCapability(sClass, powerin);
    }

    public SupernaturalClass getSupernaturalClass() {
        return this;
    }

    @Override
    public String getsClass() {
        return "Base";
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

    public abstract void castLevelOneSpell(Player player);
}

