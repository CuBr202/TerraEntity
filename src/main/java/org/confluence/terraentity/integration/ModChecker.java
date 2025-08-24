package org.confluence.terraentity.integration;

import com.google.common.base.Suppliers;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terraentity.integration.curios.CuriosEvents;
import org.confluence.terraentity.integration.iron_spell.IronSpellEvents;

import java.util.function.Supplier;

public class ModChecker {

    public static ModLoadPair confluence = create("confluence");
    public static ModLoadPair iris = create("iris");
    public static ModLoadPair irons_spellbooks = create("irons_spellbooks");
    public static ModLoadPair curios = create("curios");
    public static ModLoadPair terraCurio = create("terra_curio");
    public static ModLoadPair veil = create("veil");
    public static ModLoadPair sodiumdynamiclights = create("sodiumdynamiclights");


    public static void registerEvents(){
        if(irons_spellbooks.isLoaded()){
            NeoForge.EVENT_BUS.register(IronSpellEvents.class);
        }
        if(curios.isLoaded()){
            NeoForge.EVENT_BUS.register(CuriosEvents.class);
        }
    }


    private static ModLoadPair create(String key){
        return new ModLoadPair(key);
    }
}
