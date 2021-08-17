package com.lolzorrior.supernaturalmod.capabilities;

public interface ISupernaturalClass {
    void consumePower(int points);
    void fillPower(int points);
    void setPower(int points);
    int getPower();
    void setSupernaturalClass(String classes);
    String getSupernaturalClass();
    void setLastSpell();
    long getLastSpell();

}
