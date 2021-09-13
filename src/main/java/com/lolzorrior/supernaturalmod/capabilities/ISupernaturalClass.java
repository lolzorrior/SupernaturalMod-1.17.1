package com.lolzorrior.supernaturalmod.capabilities;

public interface ISupernaturalClass {

    void consumePower(int points);
    void fillPower(int points);
    void setPower(int points);
    int getPower();
    void setLastSpell();
    long getLastSpell();
    SupernaturalClass changeSupernaturalClass(SupernaturalClass classIn, SupernaturalClass newClass);
    SupernaturalClass getSupernaturalClass();
    String getsClass();
}
