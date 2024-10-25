package org.confluence.terraentity.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.boss.renderer.CthulhuEyeRenderer;
import org.confluence.terraentity.client.entity.renderer.DemonEyeRenderer;
import org.confluence.terraentity.init.ModEntities;


@SuppressWarnings("deprecation")
@EventBusSubscriber(modid = TerraEntity.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModClient {
/*
    public static final BlockColor HALLOW_LEAVES_COLOR = (blockState, getter, pos, tint) -> {
        if (pos == null) return -1;

        IntegerRGB x = hallowMixture(Math.abs(pos.getX()) % 12);
        IntegerRGB y = hallowMixture(Math.abs(pos.getY()) % 12);
        IntegerRGB z = hallowMixture(Math.abs(pos.getZ()) % 12);

        return x.mixture(y, 0.5F).mixture(z, 0.5F).get();
    };
    public static final ItemColor SIMPLE = (pStack, pTintIndex) -> ColoredItem.getColor(pStack);

    private static IntegerRGB hallowMixture(int m) {
        if (m <= 4) return IntegerRGB.HALLOW_A.mixture(IntegerRGB.HALLOW_B, m * 0.25F);
        if (m <= 8) return IntegerRGB.HALLOW_B.mixture(IntegerRGB.HALLOW_C, (m - 4) * 0.25F);
        return IntegerRGB.HALLOW_C.mixture(IntegerRGB.HALLOW_A, (m - 8) * 0.25F);
    }
*/


    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {

    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DEMON_EYE.get(), DemonEyeRenderer::new);
        event.registerEntityRenderer(ModEntities.CTHULHU_EYE.get(), CthulhuEyeRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {

    }




//    @SubscribeEvent
//    public static void registerTextureAtlasSpriteLoaders(RegisterTextureAtlasSpriteLoadersEvent event) {
//        event.register("still_fluid", new ITextureAtlasSpriteLoader() {
//            @Override
//            public SpriteContents loadContents(ResourceLocation name, Resource resource, FrameSize frameSize, NativeImage image, AnimationMetadataSection animationMeta, ForgeTextureMetadata forgeMeta) {
//                return new SpriteContents(name, frameSize, image, animationMeta, forgeMeta);
//            }
//
//            @Override
//            public @NotNull TextureAtlasSprite makeSprite(ResourceLocation atlasName, SpriteContents contents, int atlasWidth, int atlasHeight, int spriteX, int spriteY, int mipmapLevel) {
//                return new TextureAtlasSprite(atlasName, contents, 16, 512, 0, 0) {
//                };
//            }
//        });
//    }


}
