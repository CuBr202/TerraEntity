package org.confluence.terraentity.integration;


import net.minecraftforge.fml.ModList;

public class ModChecker {
    public static Boolean confluenceLoaded(){
        if(confluence == null){
            confluence = ModList.get().isLoaded("confluence");
        }
        return confluence;
    }
    private static Boolean confluence;


}
