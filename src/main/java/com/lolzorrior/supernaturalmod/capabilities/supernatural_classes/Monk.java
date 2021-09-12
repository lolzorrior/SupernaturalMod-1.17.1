package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Monk extends SupernaturalClass {
    private final String sClass = "Monk";
    public Monk() {
        sPower = 0;
    }

    public Monk(int i) {
        sPower = 0;
    }

    @Override
    public String getsClass() {
        return sClass;
    }
}
