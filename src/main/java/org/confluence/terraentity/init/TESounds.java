package org.confluence.terraentity.init;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;


public final class TESounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TerraEntity.MODID);

    public static final RegistryObject<SoundEvent> ROUTINE_HURT = register("routine_hurt"); // 常规受伤音效
    public static final RegistryObject<SoundEvent> ROUTINE_DEATH = register("routine_death"); // 常规死亡音效
    public static final RegistryObject<SoundEvent> DRIPPLER_HURT = register("drippler_hurt"); // 滴滴怪受伤音效
    public static final RegistryObject<SoundEvent> DRIPPLER_DEATH = register("drippler_death"); // 滴滴怪死亡音效
    public static final RegistryObject<SoundEvent> METAL_HURT = register("metal_hurt"); // 金属受伤音效
    public static final RegistryObject<SoundEvent> METAL_DEATH = register("metal_death"); // 金属死亡（爆炸）音效
    public static final RegistryObject<SoundEvent> ROAR = register("roar"); // boss吼叫
    public static final RegistryObject<SoundEvent> HURRIED_ROARING = register("hurried_roaring"); //疯狗冲刺
    public static final RegistryObject<SoundEvent> DIG_SOUND = register("dig_sound"); //蠕虫挖掘
    public static final RegistryObject<SoundEvent> USE_MOUNTS = register("use_mounts"); // 召唤坐骑
    // 血爬虫
    public static final RegistryObject<SoundEvent> BLOOD_CRAWLER_DEATH = register("blood_crawler_death");
    public static final RegistryObject<SoundEvent> BLOOD_CRAWLER_FREE = register("blood_crawler_free");
    public static final RegistryObject<SoundEvent> BLOOD_CRAWLER_HURT = register("blood_crawler_hurt");
    // 巨型卷壳怪
    public static final RegistryObject<SoundEvent> GIANT_SHELLY_DEATH = register("giant_shelly_death");
    public static final RegistryObject<SoundEvent> GIANT_SHELLY_FREE_0 = register("giant_shelly_free_0");
    public static final RegistryObject<SoundEvent> GIANT_SHELLY_FREE_1 = register("giant_shelly_free_1");
    public static final RegistryObject<SoundEvent> GIANT_SHELLY_HURT = register("giant_shelly_hurt");
    // 飞眼怪
    public static final RegistryObject<SoundEvent> VISUAL_NEURON_DEATH = register("visual_neuron_death");
    public static final RegistryObject<SoundEvent> VISUAL_NEURON_HURT = register("visual_neuron_hurt");
    // 脸怪
    public static final RegistryObject<SoundEvent> FACE_HOOT = register("face_hoot");
    public static final RegistryObject<SoundEvent> TR_ZOMBIE_DEATH = register("tr_zombie_death");
    public static final RegistryObject<SoundEvent> TR_SKELETON_HURT = register("tr_skeleton_hurt");
    // 血腥芽孢
    public static final RegistryObject<SoundEvent> BLOODY_SPORE_DEATH = register("bloody_spore_death");
    public static final RegistryObject<SoundEvent> BLOODY_SPORE_FUSE = register("bloody_spore_fuse");
    public static final RegistryObject<SoundEvent> BLOODY_SPORE_HIT = register("bloody_spore_hit");
    // 腐骴
    public static final RegistryObject<SoundEvent> DECAYEDER_AMBIENT = register("decayeder_ambient");
    public static final RegistryObject<SoundEvent> DECAYEDER_DEATH = register("decayeder_death");
    public static final RegistryObject<SoundEvent> DECAYEDER_HURT = register("decayeder_hurt");
    public static final RegistryObject<SoundEvent> DECAYEDER_STEP = register("decayeder_step");

    // 泰拉挥动
    public static final RegistryObject<SoundEvent> WAVING = register("waving");


    private static RegistryObject<SoundEvent> register(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(TerraEntity.space(id)));
    }
}
