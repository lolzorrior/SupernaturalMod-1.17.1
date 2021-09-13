package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Mage extends SupernaturalClass {
    private final String sClass = "Mage";

    public Mage() {
        sPower = 0;
    }

    public Mage(int i) {
        sPower = i;
    }


    @Override
    public String getsClass() {
        return sClass;
    }
}
