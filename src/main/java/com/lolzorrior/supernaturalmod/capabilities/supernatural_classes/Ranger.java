package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Ranger extends SupernaturalClass {
    private final String sClass = "Ranger";

    public Ranger() {
        sPower = 0;
    }

    public Ranger(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }
}
