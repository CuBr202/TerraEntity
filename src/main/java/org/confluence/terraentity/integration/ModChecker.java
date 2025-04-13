package org.confluence.terraentity.integration;

import net.neoforged.fml.ModList;

public class ModChecker {

    public static boolean confluence = false;


    public static void check(){
        confluence = ModList.get().isLoaded("confluence");
    }
}
