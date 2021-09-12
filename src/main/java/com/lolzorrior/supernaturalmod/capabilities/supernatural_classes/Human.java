package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Human extends SupernaturalClass {
    private final String sClass = "Human";
    public Human() {
        sPower = 0;
    }

    public Human(int i) {
        sPower = i;
    }

    @Override
    public String getsClass() {
        return sClass;
    }
}
