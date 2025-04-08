package org.confluence.terraentity.client.event;

import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.block.renderer.FigureBlockRenderer;
import org.confluence.terraentity.client.entity.model.*;
import org.confluence.terraentity.client.entity.renderer.ReplacedSpiderRenderer;
import org.confluence.terraentity.client.particle.BiomeColorParticle;
import org.confluence.terraentity.config.ClientConfig;
import org.confluence.terraentity.init.TEParticles;
import org.confluence.terraentity.init.block.TEFigureBlocks;
import org.confluence.terraentity.init.entity.*;

import static org.confluence.terraentity.client.util.RegisterUtils.registerModel;


@EventBusSubscriber(modid = TerraEntity.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModClientEvent {
/*
    public static final BlockColor HALLOW_LEAVES_COLOR = (blockState, getter, pos, tint) -> {
        if (pos == null) return -1;

        IntegerRGB x = hallowMixture(Math.abs(pos.getX()) % 12);
        IntegerRGB y = hallowMixture(Math.abs(pos.getY()) % 12);
        IntegerRGB z = hallowMixture(Math.abs(pos.getZ()) % 12);

        return x.mixture(y, 0.5F).mixture(z, 0.5F).getPrefab();
    };
    public static final ItemColor SIMPLE = (pStack, pTintIndex) -> ColoredItem.getColor(pStack);

    private static IntegerRGB hallowMixture(int m) {
        if (m <= 4) return IntegerRGB.HALLOW_A.mixture(IntegerRGB.HALLOW_B, m * 0.25F);
        if (m <= 8) return IntegerRGB.HALLOW_B.mixture(IntegerRGB.HALLOW_C, (m - 4) * 0.25F);
        return IntegerRGB.HALLOW_C.mixture(IntegerRGB.HALLOW_A, (m - 8) * 0.25F);
    }
*/

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientConfig.load();

        });
    }

    @SubscribeEvent
    public static void configChanged(ModConfigEvent event) {
        if(event.getConfig().getSpec() == ClientConfig.SPEC){
            ClientConfig.load();
        }
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
//        event.registerLayerDefinition(CrownOfKingSlimeModel.LAYER_LOCATION, CrownOfKingSlimeModel::createBodyLayer);
        registerModel(event, CrownOfKingSlimeModel.class);
        registerModel(event, CabbageProjModel.class);
        registerModel(event, Stinger.class);


    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

        TEMonsterEntities.registerRenderers(event);
        TEBossEntities.registerRenderers(event);
        TEProjectileEntities.registerRenderers(event);
        TESummonEntities.registerRenderers(event);
        TERideableEntities.registerRenderers(event);

        // replaced
        if (ClientConfig.ENABLE_NON_SPIDER_MODEL.get()) {
            event.registerEntityRenderer(EntityType.SPIDER, c -> new ReplacedSpiderRenderer<>(c, "spider", EntityType.SPIDER));
            event.registerEntityRenderer(EntityType.CAVE_SPIDER, c -> new ReplacedSpiderRenderer<>(c, "cave_spider", EntityType.CAVE_SPIDER));
            event.registerEntityRenderer(TEMonsterEntities.BLOOD_CRAWLER.get(), c -> new ReplacedSpiderRenderer<>(c, "blood_crawler", TEMonsterEntities.BLOOD_CRAWLER.get()));
        }

        event.registerBlockEntityRenderer(TEFigureBlocks.FIGURE_BLOCK_ENTITY.get(), c->new FigureBlockRenderer<>(c, TEMonsterEntities.NYMPH.get()));
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(TEParticles.LEAVES.get(), BiomeColorParticle.Provider::new);

    }

    @SubscribeEvent
    public static void registerAdditionalModel(ModelEvent.RegisterAdditional event) {
        WhipModelRegister.getInstance().register(event);
        EntityBlockModelRegister.getInstance().register(event);
    }



//    @SubscribeEvent
//    public static void registerTextureAtlasSpriteLoaders(RegisterTextureAtlasSpriteLoadersEvent event) {
//        event.registerEgg("still_fluid", new ITextureAtlasSpriteLoader() {
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
