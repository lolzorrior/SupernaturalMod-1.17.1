package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class Zombie extends SupernaturalClass {
    private final String sClass = "Zombie";

    public Zombie() {
        sPower = 0;
    }

    public Zombie(int i) {
        sPower = i;
    }

    public String getsClass() {
        return sClass;
    }
}
