package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Knight extends SupernaturalClass {
    private final String sClass = "Knight";

    public Knight() {
        sPower = 0;
    }

    public Knight(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }
}
