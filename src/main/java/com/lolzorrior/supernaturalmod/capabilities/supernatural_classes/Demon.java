package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Demon extends SupernaturalClass {
    private final String sClass = "Demon";

    public Demon() {
        sPower = 0;
    }

    public Demon(int i) {
        sPower = i;
    }

    @Override
    public String getsClass() {
        return sClass;
    }
}
