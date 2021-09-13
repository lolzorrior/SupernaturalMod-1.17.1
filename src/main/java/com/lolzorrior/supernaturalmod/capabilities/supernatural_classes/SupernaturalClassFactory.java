package com.lolzorrior.supernaturalmod.capabilities.supernatural_classes;

import com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass;

public class SupernaturalClassFactory {
    public static SupernaturalClass newSupernaturalClass(String in) {
        if (in.isEmpty()) {
            throw new NullPointerException();
        }
        if (in.equalsIgnoreCase("Human")) {
            return new Human();
        } else if (in.equalsIgnoreCase("Apothecary")) {
            return new Apothecary();
        } else if (in.equalsIgnoreCase("Demon")) {
            return new Demon();
        } else if (in.equalsIgnoreCase("Knight")) {
            return new Knight();
        } else if (in.equalsIgnoreCase("Mage")) {
            return new Mage();
        } else if (in.equalsIgnoreCase("Monk")) {
            return new Monk();
        } else if (in.equalsIgnoreCase("Ranger")) {
            return new Ranger();
        } else if (in.equalsIgnoreCase("Rogue")) {
            return new Rogue();
        } else if (in.equalsIgnoreCase("Warlock")) {
            return new Warlock();
        } else if (in.equalsIgnoreCase("Werewolf")) {
            return new Werewolf();
        } else if (in.equalsIgnoreCase("Witch Hunter")) {
            return new WitchHunter();
        } else if (in.equalsIgnoreCase("Zombie")) {
            return new Zombie();
        }
        return null;
    }
}
