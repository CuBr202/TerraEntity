package org.confluence.terraentity.mixin;


import com.google.common.base.Suppliers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.ClientTerraEntity;
import org.confluence.terraentity.data.security.SecurityFace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import software.bernie.geckolib.loading.FileLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Mixin(FileLoader.class)
public class FileLoaderMixin {
    @WrapOperation(method = "getFileContents", at = @At(value = "INVOKE", target = "Lorg/apache/commons/io/IOUtils;toString(Ljava/io/InputStream;Ljava/nio/charset/Charset;)Ljava/lang/String;"))
    private static String wrapToString(InputStream sw, Charset input, Operation<String> original, @Local(argsOnly = true) ResourceLocation location) {
        String result = original.call(sw, input);
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
