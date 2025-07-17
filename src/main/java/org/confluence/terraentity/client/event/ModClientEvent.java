package org.confluence.terraentity.client.event;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.attachment.ItemInHandTrailAttachment;
import org.confluence.terraentity.client.block.renderer.FigureBlockRenderer;
import org.confluence.terraentity.client.entity.model.*;
import org.confluence.terraentity.client.gui.config_container.ConfigContainerRegister;
import org.confluence.terraentity.client.gui.container.SimpleTradeScreen;
import org.confluence.terraentity.client.init.model.EntityBlockModelRegister;
import org.confluence.terraentity.client.init.model.WhipModelRegister;
import org.confluence.terraentity.client.particle.BiomeColorParticle;
import org.confluence.terraentity.client.particle.SpitParticle;
import org.confluence.terraentity.client.util.RegisterUtils;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TEMenus;
import org.confluence.terraentity.init.TEParticles;
import org.confluence.terraentity.init.block.TEFigureBlocks;
import org.confluence.terraentity.integration.sodium_dynamic_light.SDHelper;

import static org.confluence.terraentity.client.util.RegisterUtils.registerModel;


@Mod.EventBusSubscriber(modid = TerraEntity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(TEMenus.SIMPLE_NPC_TRADES_MENU.get(), SimpleTradeScreen::new);
            ItemInHandTrailAttachment.registerDefault();
            SDHelper.registerDynamicLight();
        });
    }

    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        ConfigContainerRegister.registerModsPage(event);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
//        event.registerLayerDefinition(CrownOfKingSlimeModel.LAYER_LOCATION, CrownOfKingSlimeModel::createBodyLayer);
        RegisterUtils.registerModel(event, CrownOfKingSlimeModel.class);
        RegisterUtils.registerModel(event, CabbageProjModel.class);
        RegisterUtils.registerModel(event, Stinger.class);
        registerModel(event, HarpyFeatherProjectileModel.class);
        registerModel(event, DemonScytheModel.class);
//        registerModel(event, TerraprismaModel.class);
        event.registerLayerDefinition(TerraprismaModel.LAYER_LOCATION, TerraprismaModel::createBodyLayer);

    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

        TEEntities.registerEntityRenderers(event);

        // replaced
//        if (ClientConfig.ENABLE_NON_SPIDER_MODEL.get()) {
//            event.registerEntityRenderer(EntityType.SPIDER, c -> new ReplacedSpiderRenderer<>(c, "spider", EntityType.SPIDER));
//            event.registerEntityRenderer(EntityType.CAVE_SPIDER, c -> new ReplacedSpiderRenderer<>(c, "cave_spider", EntityType.CAVE_SPIDER));
//            event.registerEntityRenderer(BLOOD_CRAWLER.get(), c -> new ReplacedSpiderRenderer<>(c, "blood_crawler", BLOOD_CRAWLER.get()));
//        }

        event.registerBlockEntityRenderer(TEFigureBlocks.FIGURE_BLOCK_ENTITY.get(), FigureBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(TEParticles.LEAVES.get(), BiomeColorParticle.Provider::new);
        event.registerSpriteSet(TEParticles.SPIT.get(), SpitParticle.Provider::new);
        event.registerSpriteSet(TEParticles.SPIT_GLOW.get(), SpitParticle.EmissiveProvider::new);

    }

    @SubscribeEvent
    public static void registerAdditionalModel(ModelEvent.RegisterAdditional event) {
        WhipModelRegister.getInstance().register(event);
        EntityBlockModelRegister.getInstance().register(event);
//        AdditionalItemRegister.getInstance().register(event);
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
