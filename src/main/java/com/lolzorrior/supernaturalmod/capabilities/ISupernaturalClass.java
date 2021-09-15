package com.lolzorrior.supernaturalmod.capabilities;

import net.minecraft.world.entity.Entity;

public interface ISupernaturalClass {

    void consumePower(int points);
    void fillPower(int points);
    void setPower(int points);
    int getPower();
    void setLastSpell();
    long getLastSpell();
    void changeSupernaturalClass(String sClassIn);
    SupernaturalClass getSupernaturalClass();
    String getsClass();

}
