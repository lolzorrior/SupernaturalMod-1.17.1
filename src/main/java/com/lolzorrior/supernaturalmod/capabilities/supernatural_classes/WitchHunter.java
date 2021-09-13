package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class WitchHunter extends SupernaturalClass {
    private final String sClass = "Witch Hunter";

    public WitchHunter() {
        sPower = 0;
    }

    public WitchHunter(int i) {
        sPower = i;
    }

    @Override
    public String getsClass() {
        return sClass;
    }
}
