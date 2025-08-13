package org.confluence.terraentity.integration;

import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terraentity.integration.curios.CuriosEvents;
import org.confluence.terraentity.integration.iron_spell.IronSpellEvents;

public class ModChecker {



    public static boolean isConfluenceLoaded = ModList.get().isLoaded("confluence");
    public static boolean isIrisLoaded = ModList.get().isLoaded("iris");
    public static boolean isIronSpellLoaded = ModList.get().isLoaded("irons_spellbooks");
    public static boolean isCuriosLoaded = ModList.get().isLoaded("curios");


    public static void registerEvents(){
        if(isIronSpellLoaded){
            NeoForge.EVENT_BUS.register(IronSpellEvents.class);
        }
        if(isCuriosLoaded){
            NeoForge.EVENT_BUS.register(CuriosEvents.class);
        }
    }


}
