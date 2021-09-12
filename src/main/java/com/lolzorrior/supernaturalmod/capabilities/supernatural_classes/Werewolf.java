package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Werewolf extends SupernaturalClass {
    private final String sClass = "Werewolf";

    public Werewolf() {
        sPower = 0;
    }

    public Werewolf(int i) {
        sPower = i;
    }

    @Override
    public String getsClass() {
        return sClass;
    }
}
