package org.confluence.terraentity.client;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

public class ClientTerraEntity {


    public static Supplier<String> seKey;
    public static boolean shouldSe(ResourceLocation location, String result){
        String namespace = location.getNamespace();
        if(!namespace.equals(MODID)){
            return false;
        }
//        String path = location.getPath();
//        if(path.startsWith("geo/entity/boss")){
//            return true;
//        }
        if(!result.startsWith("{")){
            return true;
        }
        return false;
    }
}
