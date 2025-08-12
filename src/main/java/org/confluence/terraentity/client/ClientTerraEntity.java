package org.confluence.terraentity.client;

import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.security.SecurityFace;

import java.io.IOException;
import java.io.InputStream;
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

    public static String wrapFile(String result, ResourceLocation location) {
        // Please respect the copyright of the model and do not attempt to publicly disseminate encrypted files.
        // 请尊重模型的著作权，加密文件请不要试图公开传播。
        if(ClientTerraEntity.shouldSe(location, result)){
            if(ClientTerraEntity.seKey == null){
                ClientTerraEntity.seKey = Suppliers.memoize(()->{
                    try {
                        ResourceLocation location1 = TerraEntity.space("license.bin");
                        InputStream inputStream = Minecraft.getInstance().getResourceManager().open(location1);
                        return SecurityFace.readKey(inputStream, i-> location1.hashCode());
                    } catch (IOException e){
                        throw new RuntimeException("License Key Error");
                    }
                });
            }
            return SecurityFace.S3.decrypt(result, ClientTerraEntity.seKey.get());
        }
        return result;
    }
}
