package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Rogue extends SupernaturalClass {
    private final String sClass = "Rogue";

    public Rogue() {
        sPower = 0;
    }

    public Rogue(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }
}
