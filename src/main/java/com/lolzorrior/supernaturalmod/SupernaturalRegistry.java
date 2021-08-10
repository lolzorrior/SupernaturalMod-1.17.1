package com.lolzorrior.supernaturalmod;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;
import net.minecraft.server.Bootstrap;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class SupernaturalRegistry {
    static {init();}
    public static final IForgeRegistry<SupernaturalClass> SUPERNATURAL_CLASSES = RegistryManager.ACTIVE.getRegistry(SupernaturalClass.class);

    private static void init()
    {
        GameData.init();
        Bootstrap.bootStrap();
        Tags.init();
    }
}


