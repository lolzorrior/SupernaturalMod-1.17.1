package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Warlock extends SupernaturalClass {
    private final String sClass = "Warlock";

    public Warlock() {
        sPower = 0;
    }

    public Warlock(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }
}
