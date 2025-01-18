package org.confluence.terraentity.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.TerraEntity;

import java.io.IOException;

import static org.confluence.terraentity.TerraEntity.MODID;

public final class ModRenderTypes {
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Shaders {
        public static ShaderInstance floatBarShader;


        @SubscribeEvent
        public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
            ResourceProvider resourceProvider = event.getResourceProvider();

//            event.registerShader(new ShaderInstance(resourceProvider,
//                    TerraEntity.space("float_bar"),
//                    DefaultVertexFormat.POSITION_TEX_COLOR),
//                    shader -> floatBarShader = shader);

        }
    }

}
