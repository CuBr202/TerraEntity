package org.confluence.terraentity.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.entity.model.CabbageProjModel;
import org.confluence.terraentity.client.entity.model.CrownOfKingSlimeModel;
import org.confluence.terraentity.client.entity.model.Stinger;
import org.confluence.terraentity.client.entity.model.WhipModelRegister;
import org.confluence.terraentity.client.entity.renderer.CrownOfKingSlimeModelRenderer;
import org.confluence.terraentity.client.entity.renderer.ProjRenderer;
import org.confluence.terraentity.client.gui.config_container.ConfigContainerRegister;
import org.confluence.terraentity.client.particle.BiomeColorParticle;
import org.confluence.terraentity.entity.proj.BaseProj;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TEParticles;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.confluence.terraentity.init.TEEntities.*;


@Mod.EventBusSubscriber(modid = TerraEntity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModClient {
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
public static void onClientSetup(final FMLClientSetupEvent evt) {
//        ModList.get().getModContainerById(MODID).ifPresent(container -> {
//            container.registerExtensionPoint(
//                    ConfigScreenHandler.ConfigScreenFactory.class,
//                    () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new ConfigScreen(screen)));
//        });
}
    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        ConfigContainerRegister.registerModsPage(event);
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
        event.registerEntityRenderer(CROWN_OF_KING_SLIME_MODEL.get(), CrownOfKingSlimeModelRenderer::new);

        registerProj(event,CABBAGE_PROJ.get(),c->new CabbageProjModel<>(c.bakeLayer(CabbageProjModel.LAYER_LOCATION)));
        registerProj(event,BEE_STICK_PROJ.get(),c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));
        registerProj(event,SUMMON_BEE_STICK_PROJ.get(),c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));

        // replaced
//        if (ClientConfig.ENABLE_NON_SPIDER_MODEL.get()) {
//            event.registerEntityRenderer(EntityType.SPIDER, c -> new ReplacedSpiderRenderer<>(c, "spider", EntityType.SPIDER));
//            event.registerEntityRenderer(EntityType.CAVE_SPIDER, c -> new ReplacedSpiderRenderer<>(c, "cave_spider", EntityType.CAVE_SPIDER));
//            event.registerEntityRenderer(BLOOD_CRAWLER.get(), c -> new ReplacedSpiderRenderer<>(c, "blood_crawler", BLOOD_CRAWLER.get()));
//        }

        TEEntities.registerRenderers(event);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(TEParticles.LEAVES.get(), BiomeColorParticle.Provider::new);

    }

    @SubscribeEvent
    public static void registerAdditionalModel(ModelEvent.RegisterAdditional event) {
        ResourceManager provider = Minecraft.getInstance().getResourceManager();
        provider.listResources("models/item/whip", s -> s.getPath().endsWith(".json")).forEach((location, resource) -> {
            var f = WhipModelRegister.getInstance().process(location);
            if(f!= null){
                event.register(f);
            }else{
                TerraEntity.LOGGER.warn("Failed to load whip model: {}", location);
            }
        });
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


    public static ModelLayerLocation getModelDefine(Class<? extends Model> clz){
        Field field2;
        try{
            field2  = clz.getDeclaredField("LAYER_LOCATION");
        }catch (Exception e){ throw new RuntimeException();}
        field2.setAccessible(true);

        try{
            return (ModelLayerLocation) field2.get(null);
        }catch (Exception e){ throw new RuntimeException();}

    }

    public static Supplier<LayerDefinition> getLayerDefinition(Class<? extends Model> clz){
        return  ()-> {
            try {
                return (LayerDefinition) clz.getMethod("createBodyLayer").invoke(null);
            } catch (Exception e) {throw new RuntimeException(e);}
        };
    }

    public static void registerModel(EntityRenderersEvent.RegisterLayerDefinitions evt, Class<? extends Model> clz){
        evt.registerLayerDefinition(getModelDefine(clz), getLayerDefinition(clz));
    }

    public static <T extends BaseProj>void registerProj(EntityRenderersEvent.RegisterRenderers event, EntityType<T> entityType, Function<EntityRendererProvider.Context, EntityModel<T>> model){
        event.registerEntityRenderer(entityType, (dispatcher)-> new ProjRenderer<>(dispatcher, model.apply(dispatcher),1,0));
    }

    public static <T extends BaseProj>void registerProj(EntityRenderersEvent.RegisterRenderers event, EntityType<T> entityType, Function<EntityRendererProvider.Context, EntityModel<T>> model, float size, float offsetY){
        event.registerEntityRenderer(entityType, (dispatcher)-> new ProjRenderer<>(dispatcher, model.apply(dispatcher),size,offsetY));
    }
}
