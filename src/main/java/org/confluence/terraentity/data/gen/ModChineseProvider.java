package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terraentity.init.ModItems;

import static org.confluence.terraentity.TerraEntity.MODID;


public class ModChineseProvider extends LanguageProvider {
    public ModChineseProvider(PackOutput output) {
        super(output, MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.terraentity.title", "泰拉生物");
        // 刷怪蛋
        add(ModItems.BLUE_SLIME_SPAWN_EGG.get(), "蓝色史莱姆刷怪蛋");
        add(ModItems.RED_SLIME_SPAWN_EGG.get(), "红色史莱姆刷怪蛋");
        add(ModItems.YELLOW_SLIME_SPAWN_EGG.get(), "黄色史莱姆刷怪蛋");
        add(ModItems.HONEY_SLIME_SPAWN_EGG.get(), "蜂蜜史莱姆刷怪蛋");
        add(ModItems.PURPLE_SLIME_SPAWN_EGG.get(), "紫色史莱姆刷怪蛋");
        add(ModItems.DESERT_SLIME_SPAWN_EGG.get(), "沙漠史莱姆刷怪蛋");
        add(ModItems.JUNGLE_SLIME_SPAWN_EGG.get(), "丛林史莱姆刷怪蛋");
        add(ModItems.PINK_SLIME_SPAWN_EGG.get(), "粉色史莱姆刷怪蛋");
        add(ModItems.ICE_SLIME_SPAWN_EGG.get(), "冰冻史莱姆刷怪蛋");
        add(ModItems.GREEN_SLIME_SPAWN_EGG.get(), "绿色史莱姆刷怪蛋");
        add(ModItems.BLACK_SLIME_SPAWN_EGG.get(), "史莱姆之母刷怪蛋");
        add(ModItems.CRIMSON_SLIME_SPAWN_EGG.get(), "猩红史莱姆刷怪蛋");
        add(ModItems.TROPIC_SLIME_SPAWN_EGG.get(), "热带史莱姆刷怪蛋");
        add(ModItems.LUMINOUS_SLIME_SPAWN_EGG.get(), "夜明史莱姆刷怪蛋");
        add(ModItems.LAVA_SLIME_SPAWN_EGG.get(), "岩浆史莱姆刷怪蛋");

        add(ModItems.DEMON_EYE_SPAWN_EGG.get(), "恶魔眼刷怪蛋");
        add(ModItems.FLYING_FISH_SPAWN_EGG.get(), "飞鱼刷怪蛋");
        add(ModItems.DRIPPLER_SPAWN_EGG.get(), "滴滴怪刷怪蛋");

        add(ModItems.BLOOD_CRAWLER_SPAWN_EGG.get(), "血爬虫刷怪蛋");
        add(ModItems.BLOODY_SPORE_SPAWN_EGG.get(), "血腥芽孢刷怪蛋");
        add(ModItems.CRIMSON_KEMERA_EGG.get(), "喀迈拉刷怪蛋");


        add(ModItems.KING_SLIME_SPAWN_EGG.get(), "史莱姆王刷怪蛋");
        add(ModItems.CTHULHU_EYE_SPAWN_EGG.get(), "克苏鲁之眼刷怪蛋");



    }
}
