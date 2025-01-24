package org.confluence.terraentity.client.post;

import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.Minecraft;
import org.confluence.terraentity.client.ModRenderTypes;
import org.confluence.terraentity.client.util.ShaderUtil;
import org.confluence.terraentity.entity.boss.BrainOfCthulhu;

import java.util.HashMap;
import java.util.Map;

public class BrainTranslucent {
    public static class tuple{
        public TextureTarget target;
        public float alpha;
        public tuple(TextureTarget target, float alpha){
            this.target = target;
            this.alpha = alpha;
        }
    }
    public static Map<BrainOfCthulhu, tuple> entityMap = new HashMap<>();

    public static void render(){

        if(entityMap.isEmpty()) return;

        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        for(BrainOfCthulhu brain : entityMap.keySet()){
            if(brain!= null && brain.isAlive()){
                tuple tuple = entityMap.get(brain);
                TextureTarget target = tuple.target;
//                target.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
                ShaderUtil.blitScreen(ModRenderTypes.Shaders.colorBlitShader,shader->{
                    shader.COLOR_MODULATOR.set(1f, 1f, 1f, tuple.alpha);
                    shader.setSampler("Sampler0", Minecraft.getInstance().getMainRenderTarget());
                    shader.setSampler("Sampler1", target);
                });
                target.clear(true);
            }else{
                entityMap.remove(brain);
            }
        }
    }
}
