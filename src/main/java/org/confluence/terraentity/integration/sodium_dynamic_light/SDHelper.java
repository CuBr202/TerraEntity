package org.confluence.terraentity.integration.sodium_dynamic_light;

import com.google.common.base.Suppliers;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandler;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.minecraftforge.fml.ModList;
import org.confluence.terraentity.init.entity.TESummonEntities;

import java.util.function.Supplier;

public class SDHelper {

    public static Supplier<Boolean> isLoaded = Suppliers.memoize(()-> ModList.get().isLoaded("sodiumdynamiclights"));



    public static void registerDynamicLight(){
        if(!isLoaded.get()){
            return;
        }
        DynamicLightHandlers.registerDynamicLightHandler(TESummonEntities.TERRAPRISMA.get(), (DynamicLightHandler) o -> 15);


    }
}
