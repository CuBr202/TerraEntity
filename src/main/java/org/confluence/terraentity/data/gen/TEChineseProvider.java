package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TEItems;

import static org.confluence.terraentity.TerraEntity.MODID;


public class TEChineseProvider extends LanguageProvider {
    public TEChineseProvider(PackOutput output) {
        super(output, MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.terraentity.title", "泰拉生物");


        add("entity.terra_entity.ice_slime", "冰冻史莱姆");
        add("entity.terra_entity.blue_slime", "蓝色史莱姆");
        add("entity.terra_entity.red_slime", "红色史莱姆");
        add("entity.terra_entity.purple_slime", "紫色史莱姆");
        add("entity.terra_entity.jungle_slime", "丛林史莱姆");
        add("entity.terra_entity.pink_slime", "粉色史莱姆");
        add("entity.terra_entity.yellow_slime", "黄色史莱姆");
        add("entity.terra_entity.honey_slime", "蜂蜜史莱姆");
        add("entity.terra_entity.crimson_slime", "猩红史莱姆");
        add("entity.terra_entity.corrupted_slime", "腐化史莱姆");
        add("entity.terra_entity.desert_slime", "沙漠史莱姆");
        add("entity.terra_entity.tropic_slime", "热带史莱姆");
        add("entity.terra_entity.green_slime", "绿色史莱姆");
        add("entity.terra_entity.black_slime", "史莱姆之母");
        add("entity.terra_entity.lava_slime", "熔岩史莱姆");
        add("entity.terra_entity.demon_eye", "恶魔眼");
        add("entity.terra_entity.flying_fish", "飞鱼");
        add("entity.terra_entity.giant_shelly", "巨型卷壳怪");
        add("entity.terra_entity.drippler", "滴滴怪");
        add("entity.terra_entity.blood_zombie", "血腥僵尸");
        add("entity.terra_entity.blood_crawler", "血爬虫");
        add("entity.terra_entity.bloody_spore", "血腥芽孢");
        add("entity.terra_entity.face_monster", "脸怪");
        add("entity.terra_entity.crimson_kemera", "猩红喀迈拉");
        add("entity.terra_entity.eater_of_souls", "噬魂怪");
        add("entity.terra_entity.decayeder", "腐骴");
        add("entity.terra_entity.devourer", "吞噬怪");
        add("entity.terra_entity.giant_worm", "巨型蠕虫");
        add("entity.terra_entity.tomb_crawler", "墓穴爬虫");
        add("entity.terra_entity.cave_bat", "洞穴蝙蝠");
        add("entity.terra_entity.jungle_bat", "丛林蝙蝠");
        add("entity.terra_entity.hornet", "黄蜂");
        add("entity.terra_entity.hell_bat", "地狱蝙蝠");
        add("entity.terra_entity.ice_bat", "冰雪蝙蝠");
        add("entity.terra_entity.spore_bat", "孢子蝙蝠");


        add("entity.terra_entity.king_slime", "史莱姆王");
        add("entity.terra_entity.eye_of_cthulhu", "克苏鲁之眼");
        add("entity.terra_entity.eater_of_worlds", "世界吞噬怪");
        add("entity.terra_entity.eater_of_worlds_segment", "世界吞噬怪体节");
        add("entity.terra_entity.brain_of_cthulhu", "克苏鲁之脑");
        add("entity.terra_entity.brain_fake", "克苏鲁之脑幻象");
        add("entity.terra_entity.visual_neuron", "视神经元");
        add("entity.terra_entity.queen_bee", "蜂王");
        add("entity.terra_entity.little_hornet", "小黄蜂");
        add("entity.terra_entity.skeletron", "骷髅王");

        add("effect.terra_entity.demonic_thoughts", "邪念");

        add(TEEntities.SUMMON_SLIME.get(), "史莱姆宝宝");
        add(TEEntities.SUMMON_IRON_GOLEM.get(), "i-32型铁傀儡");
        add(TEEntities.SUMMON_HORNET.get(), "仆役黄蜂");


        // 刷怪蛋
        add(TEItems.BLUE_SLIME_SPAWN_EGG.get(), "蓝色史莱姆刷怪蛋");
        add(TEItems.RED_SLIME_SPAWN_EGG.get(), "红色史莱姆刷怪蛋");
        add(TEItems.YELLOW_SLIME_SPAWN_EGG.get(), "黄色史莱姆刷怪蛋");
        add(TEItems.HONEY_SLIME_SPAWN_EGG.get(), "蜂蜜史莱姆刷怪蛋");
        add(TEItems.PURPLE_SLIME_SPAWN_EGG.get(), "紫色史莱姆刷怪蛋");
        add(TEItems.DESERT_SLIME_SPAWN_EGG.get(), "沙漠史莱姆刷怪蛋");
        add(TEItems.JUNGLE_SLIME_SPAWN_EGG.get(), "丛林史莱姆刷怪蛋");
        add(TEItems.PINK_SLIME_SPAWN_EGG.get(), "粉色史莱姆刷怪蛋");
        add(TEItems.ICE_SLIME_SPAWN_EGG.get(), "冰冻史莱姆刷怪蛋");
        add(TEItems.GREEN_SLIME_SPAWN_EGG.get(), "绿色史莱姆刷怪蛋");
        add(TEItems.BLACK_SLIME_SPAWN_EGG.get(), "史莱姆之母刷怪蛋");
        add(TEItems.CRIMSON_SLIME_SPAWN_EGG.get(), "猩红史莱姆刷怪蛋");
        add(TEItems.TROPIC_SLIME_SPAWN_EGG.get(), "热带史莱姆刷怪蛋");
        add(TEItems.LUMINOUS_SLIME_SPAWN_EGG.get(), "夜明史莱姆刷怪蛋");
        add(TEItems.LAVA_SLIME_SPAWN_EGG.get(), "熔岩史莱姆刷怪蛋");

        add(TEItems.DEMON_EYE_SPAWN_EGG.get(), "恶魔眼刷怪蛋");
        add(TEItems.FLYING_FISH_SPAWN_EGG.get(), "飞鱼刷怪蛋");
        add(TEItems.GIANT_SHELLY_SPAWN_EGG.get(), "巨型卷壳怪刷怪蛋");
        add(TEItems.GIANT_WORM_SPAWN_EGG.get(), "巨型蠕虫刷怪蛋");
        add(TEItems.TOMB_CRAWLER_SPAWN_EGG.get(), "墓穴爬虫刷怪蛋");
        add(TEItems.CAVE_BAT_SPAWN_EGG.get(), "洞穴蝙蝠刷怪蛋");
        add(TEItems.ICE_BAT_SPAWN_EGG.get(), "冰雪蝙蝠刷怪蛋");
        add(TEItems.JUNGLE_BAT_SPAWN_EGG.get(), "丛林蝙蝠刷怪蛋");
        add(TEItems.HORNET_SPAWN_EGG.get(), "黄蜂刷怪蛋");
        add(TEItems.HELL_BAT_SPAWN_EGG.get(), "地狱蝙蝠刷怪蛋");
        add(TEItems.SPORE_BAT_SPAWN_EGG.get(), "孢子蝙蝠刷怪蛋");
        add(TEItems.DRIPPLER_SPAWN_EGG.get(), "滴滴怪刷怪蛋");
        add(TEItems.BLOOD_ZOMBIE_SPAWN_EGG.get(), "血腥僵尸刷怪蛋");

        add(TEItems.BLOOD_CRAWLER_SPAWN_EGG.get(), "血爬虫刷怪蛋");
        add(TEItems.BLOODY_SPORE_SPAWN_EGG.get(), "血腥芽孢刷怪蛋");
        add(TEItems.CRIMSON_KEMERA_EGG.get(), "猩红喀迈拉刷怪蛋");
        add(TEItems.FACE_MONSTER_EGG.get(), "脸怪刷怪蛋");

        add(TEItems.EATER_OF_SOULS_SPAWN_EGG.get(), "噬魂怪刷怪蛋");
        add(TEItems.DECAYEDER_SPAWN_EGG.get(), "腐骴刷怪蛋");
        add(TEItems.DEVOURER_SPAWN_EGG.get(), "吞噬怪刷怪蛋");

        add(TEItems.KING_SLIME_SPAWN_EGG.get(), "史莱姆王刷怪蛋");
        add(TEItems.EYE_OF_CTHULHU_SPAWN_EGG.get(), "克苏鲁之眼刷怪蛋");
        add(TEItems.EATER_OF_WORLD_SPAWN_EGG.get(), "世界吞噬怪刷怪蛋");
        add(TEItems.BRAIN_OF_CTHULHU_SPAWN_EGG.get(), "克苏鲁之脑刷怪蛋");
        add(TEItems.QUEEN_BEE_SPAWN_EGG.get(), "蜂王刷怪蛋");


        // 召唤杖
        add(TEItems.SLIME_STAFF.get(), "史莱姆法杖");
        add(TEItems.IRON_GOLEM_STAFF.get(), "铁傀儡法杖");
        add(TEItems.HORNET_STAFF.get(), "黄蜂法杖");

        // 鞭子
        add(TEItems.SWAMP_WHIP.get(), "沼泽藤蔓");



        add("message.terraentity.boss_spawn", "%s已苏醒！");
        add("message.terraentity.boss_leave", "%s已被打败！");
        add("message.terraentity.boss_discard", "已离开！");


        add("terra_entity.options.title", "Terra Entity 配置");
        add("terra_entity.configuration.server", "服务端配置");
        add("terra_entity.configuration.client", "客户端配置");

        // config
        add("terra_entity.configuration.server.boss_clear_when_no_target", "丢失目标时清除BOSS");
        add("terra_entity.configuration.server.boss_attributes_multiplier_health", "BOSS属性倍率-生命");
        add("terra_entity.configuration.server.boss_attributes_multiplier_damage", "BOSS属性倍率-伤害");
        add("terra_entity.configuration.server.boss_no_physics", "BOSS能否穿墙");
        add("terra_entity.configuration.server.boss_leave_on_day", "特定的BOSS是否在白天离开");


        add("terra_entity.configuration.server.display_summon_items", "在创造栏显示召唤物品");
        add("terra_entity.configuration.server.enhance_all_monster", "增强所有怪物");
        add("terra_entity.configuration.server.monster_attributes_multiplier_health", "Monster属性倍率-生命");
        add("terra_entity.configuration.server.monster_attributes_multiplier_damage", "Monster属性倍率-伤害");

        add("terra_entity.configuration.client.boss_bar_style", "BOSS血条样式");
        add("terra_entity.configuration.client.boss_bar_number_offset_x", "BOSS血条数字偏移-X");
        add("terra_entity.configuration.client.boss_bar_number_offset_y", "BOSS血条数字偏移-Y");
        add("terra_entity.configuration.client.enableNonSpiderModel", "蜘蛛和谐");
        add("terra_entity.configuration.client.enableNonSpiderModel.tooltip", "如果你对蜘蛛反感或想美化他们，请开启这个选项");


        // Tooltip
        add("tooltip.terra_entity.summon_item_cost", "仆从占用: %s");
        add("tooltip.terra_entity.summon_item_entity", "仆从类型: %s");
        add("tooltip.terra_entity.summon_info", "仆从栏位: %d / %d");


        // attribute
        add("attribute.name.player.summon_damage", "召唤伤害");
        add("attribute.name.player.mark_damage", "标记伤害");
        add("attribute.name.player.whip_range", "鞭范围");
        add("attribute.name.player.summon_knockback", "召唤物击退");
        add("attribute.name.player.minion_capacity", "仆从容量");
        add("attribute.name.player.sentry_capacity", "哨兵容量");
        // track
        add("terra_entity.track_type.simple", "简单");
        add("terra_entity.track_type.basis", "基平面");

    }
}
