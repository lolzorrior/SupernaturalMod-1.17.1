package com.lolzorrior.supernaturalmod.util;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClassStorage;

public class LazyOptionalUtil {
    private static SupernaturalClassStorage supernaturalClassStorage = new SupernaturalClassStorage();

    public static SupernaturalClassStorage getStorage() {
        return supernaturalClassStorage;
    }
}
