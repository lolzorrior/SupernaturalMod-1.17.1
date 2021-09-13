package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Apothecary extends SupernaturalClass {
    private final String sClass = "Apothecary";

    public Apothecary() {
        sPower = 0;
    }

    public Apothecary(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }
}
