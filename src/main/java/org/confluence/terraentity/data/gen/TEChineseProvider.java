package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TEItems;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.init.entity.TERideableEntities;
import org.confluence.terraentity.init.entity.TESummonEntities;
import org.confluence.terraentity.init.item.*;

import static org.confluence.terraentity.TerraEntity.MODID;


public class TEChineseProvider extends LanguageProvider {
    public TEChineseProvider(PackOutput output) {
        super(output, MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.terraentity.title", "泰拉生物");

        add("title.terra_entity.npc_trade", "泰拉商店");



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
        add("entity.terra_entity.black_slime", "黑色史莱姆");
        add("entity.terra_entity.mother_slime", "史莱姆之母");
        add("entity.terra_entity.baby_slime", "史莱姆宝宝");
        add("entity.terra_entity.lava_slime", "熔岩史莱姆");
        add("entity.terra_entity.green_dumpling_slime", "青团史莱姆");
        add("entity.terra_entity.swamp_slime", "沼泽史莱姆");
        add("entity.terra_entity.demon_eye", "恶魔眼");
        add("entity.terra_entity.flying_fish", "飞鱼");
        add("entity.terra_entity.giant_shelly", "巨型卷壳怪");
        add("entity.terra_entity.nymph", "宁芙");
        add("entity.terra_entity.drippler", "滴滴怪");
        add("entity.terra_entity.blood_zombie", "血腥僵尸");
        add("entity.terra_entity.wandering_eye_fish", "游荡眼球怪鱼");
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
        add("entity.terra_entity.snatcher", "抓人草");
        add("entity.terra_entity.man_eater", "食人怪");
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


        add(TESummonEntities.SUMMON_SLIME.get(), "史莱姆宝宝");
        add(TESummonEntities.SUMMON_IRON_GOLEM.get(), "i-32型铁傀儡");
        add(TESummonEntities.SUMMON_HORNET.get(), "仆役黄蜂");

        add(TERideableEntities.RIDEABLE_SLIME.get(), "史莱姆坐骑");
        add(TERideableEntities.RIDEABLE_BEE.get(), "蜜蜂坐骑");

        add(TENpcEntities.GUIDE.get(), "向导");
        add(TENpcEntities.DEMOLITIONIST.get(), "爆破专家");

        // 刷怪蛋
        add(TESpawnEggItems.BLUE_SLIME_SPAWN_EGG.get(), "蓝色史莱姆刷怪蛋");
        add(TESpawnEggItems.RED_SLIME_SPAWN_EGG.get(), "红色史莱姆刷怪蛋");
        add(TESpawnEggItems.YELLOW_SLIME_SPAWN_EGG.get(), "黄色史莱姆刷怪蛋");
        add(TESpawnEggItems.HONEY_SLIME_SPAWN_EGG.get(), "蜂蜜史莱姆刷怪蛋");
        add(TESpawnEggItems.PURPLE_SLIME_SPAWN_EGG.get(), "紫色史莱姆刷怪蛋");
        add(TESpawnEggItems.DESERT_SLIME_SPAWN_EGG.get(), "沙漠史莱姆刷怪蛋");
        add(TESpawnEggItems.GREEN_DUMPLING_SLIME_SPAWN_EGG.get(), "青团史莱姆刷怪蛋");
        add(TESpawnEggItems.SWAMP_SLIME_SPAWN_EGG.get(), "沼泽史莱姆刷怪蛋");
        add(TESpawnEggItems.JUNGLE_SLIME_SPAWN_EGG.get(), "丛林史莱姆刷怪蛋");
        add(TESpawnEggItems.PINK_SLIME_SPAWN_EGG.get(), "粉色史莱姆刷怪蛋");
        add(TESpawnEggItems.ICE_SLIME_SPAWN_EGG.get(), "冰冻史莱姆刷怪蛋");
        add(TESpawnEggItems.GREEN_SLIME_SPAWN_EGG.get(), "绿色史莱姆刷怪蛋");
        add(TESpawnEggItems.BLACK_SLIME_SPAWN_EGG.get(), "黑色史莱姆刷怪蛋");
        add(TESpawnEggItems.CRIMSON_SLIME_SPAWN_EGG.get(), "猩红史莱姆刷怪蛋");
        add(TESpawnEggItems.TROPIC_SLIME_SPAWN_EGG.get(), "热带史莱姆刷怪蛋");
        add(TESpawnEggItems.LUMINOUS_SLIME_SPAWN_EGG.get(), "夜明史莱姆刷怪蛋");
        add(TESpawnEggItems.LAVA_SLIME_SPAWN_EGG.get(), "熔岩史莱姆刷怪蛋");

        add(TESpawnEggItems.DEMON_EYE_SPAWN_EGG.get(), "恶魔眼刷怪蛋");
        add(TESpawnEggItems.FLYING_FISH_SPAWN_EGG.get(), "飞鱼刷怪蛋");
        add(TESpawnEggItems.GIANT_SHELLY_SPAWN_EGG.get(), "巨型卷壳怪刷怪蛋");
        add(TESpawnEggItems.GIANT_WORM_SPAWN_EGG.get(), "巨型蠕虫刷怪蛋");
        add(TESpawnEggItems.NYMPH_SPAWN_EGG.get(), "宁芙刷怪蛋");
        add(TESpawnEggItems.TOMB_CRAWLER_SPAWN_EGG.get(), "墓穴爬虫刷怪蛋");
        add(TESpawnEggItems.CAVE_BAT_SPAWN_EGG.get(), "洞穴蝙蝠刷怪蛋");
        add(TESpawnEggItems.ICE_BAT_SPAWN_EGG.get(), "冰雪蝙蝠刷怪蛋");
        add(TESpawnEggItems.JUNGLE_BAT_SPAWN_EGG.get(), "丛林蝙蝠刷怪蛋");
        add(TESpawnEggItems.SNATCHER_SPAWN_EGG.get(), "抓人草刷怪蛋");
        add(TESpawnEggItems.MAN_EATER_SPAWN_EGG.get(), "食人怪刷怪蛋");
        add(TESpawnEggItems.HORNET_SPAWN_EGG.get(), "黄蜂刷怪蛋");
        add(TESpawnEggItems.HELL_BAT_SPAWN_EGG.get(), "地狱蝙蝠刷怪蛋");
        add(TESpawnEggItems.SPORE_BAT_SPAWN_EGG.get(), "孢子蝙蝠刷怪蛋");
        add(TESpawnEggItems.DRIPPLER_SPAWN_EGG.get(), "滴滴怪刷怪蛋");
        add(TESpawnEggItems.BLOOD_ZOMBIE_SPAWN_EGG.get(), "血腥僵尸刷怪蛋");
        add(TESpawnEggItems.WANDERING_EYE_FISH_SPAWN_EGG.get(), "游荡眼球怪鱼刷怪蛋");

        add(TESpawnEggItems.BLOOD_CRAWLER_SPAWN_EGG.get(), "血爬虫刷怪蛋");
        add(TESpawnEggItems.BLOODY_SPORE_SPAWN_EGG.get(), "血腥芽孢刷怪蛋");
        add(TESpawnEggItems.CRIMSON_KEMERA_EGG.get(), "猩红喀迈拉刷怪蛋");
        add(TESpawnEggItems.FACE_MONSTER_EGG.get(), "脸怪刷怪蛋");

        add(TESpawnEggItems.EATER_OF_SOULS_SPAWN_EGG.get(), "噬魂怪刷怪蛋");
        add(TESpawnEggItems.DECAYEDER_SPAWN_EGG.get(), "腐骴刷怪蛋");
        add(TESpawnEggItems.DEVOURER_SPAWN_EGG.get(), "吞噬怪刷怪蛋");

        add(TESpawnEggItems.KING_SLIME_SPAWN_EGG.get(), "史莱姆王刷怪蛋");
        add(TESpawnEggItems.EYE_OF_CTHULHU_SPAWN_EGG.get(), "克苏鲁之眼刷怪蛋");
        add(TESpawnEggItems.EATER_OF_WORLD_SPAWN_EGG.get(), "世界吞噬怪刷怪蛋");
        add(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG.get(), "克苏鲁之脑刷怪蛋");
        add(TESpawnEggItems.QUEEN_BEE_SPAWN_EGG.get(), "蜂王刷怪蛋");

        add(TESpawnEggItems.GUILD_EGG.get(), "向导刷怪蛋");
        add(TESpawnEggItems.DEMOLITIONIST_EGG.get(), "爆破专家刷怪蛋");


        // 召唤杖
        add(TESummonItems.SLIME_STAFF.get(), "史莱姆法杖");
        add(TESummonItems.IRON_GOLEM_STAFF.get(), "铁傀儡法杖");
        add(TESummonItems.HORNET_STAFF.get(), "黄蜂法杖");

        // 鞭子
        add(TEWhipItems.LEATHER_WHIP.get(), "皮鞭");
        add(TEWhipItems.SLUB_WHIP.get(), "竹节鞭");
        add(TEWhipItems.RUBY_WHIP.get(), "红玉鞭");
        add(TEWhipItems.AMBER_WHIP.get(), "琥珀鞭");
        add(TEWhipItems.TOPAZ_WHIP.get(), "黄玉鞭");
        add(TEWhipItems.EMERALD_WHIP.get(), "翡翠鞭");
        add(TEWhipItems.DIAMOND_WHIP.get(), "钻石鞭");
        add(TEWhipItems.SAPPHIRE_WHIP.get(), "蓝玉鞭");
        add(TEWhipItems.AMETHYST_WHIP.get(), "紫晶鞭");
        add(TEWhipItems.SWAMP_WHIP.get(), "沼泽藤蔓");

        // 回旋镖
        add(TEBoomerangItems.WOOD_BOOMERANG.get(), "木回旋镖");
        add(TEBoomerangItems.ENCHANTED_BOOMERANG.get(), "附魔回旋镖");
        add(TEBoomerangItems.SHROOMERANG.get(), "蘑菇回旋镖");
        add(TEBoomerangItems.ICE_BOOMERANG.get(), "冰雪回旋镖");
        add(TEBoomerangItems.TRIMARANG.get(), "三尖回旋镖");
        add(TEBoomerangItems.FLAMARANG.get(), "烈焰回旋镖");
        add(TEBoomerangItems.DEVELOPER_BOOMERANG.get(), "开发者回旋镖");
        add(TEBoomerangItems.BeiDou_BOOMERANG.get(), "北斗飞镖");

        // 骑乘
        add(TERideableItems.SLIMY_SADDLE.get(), "粘鞍");
        add(TERideableItems.HONEYED_GOGGLES.get(), "凃蜜护目镜");

        // mob_effect
        add(TEEffects.DEMONIC_THOUGHTS.get(), "邪念");
        add(TEEffects.SUMMON_FOCUS.get(), "狩猎");
        add(TEEffects.HELLFIRE.get(), "狱炎");
        add(TEEffects.FROST_BURN.get(), "霜冻");

        add(TEItems.HOUSE_DETECTOR.get(), "房屋探测器");


        add("message.terraentity.boss_spawn", "%s已苏醒！");
        add("message.terraentity.boss_leave", "%s已被打败！");
        add("message.terraentity.boss_discard", "已离开！");
        add("message.terra_entity.trade.not_enough_items", "你没有足够的物品来交易");


        // config
        add("terra_entity.configuration.boss_clear_when_no_target", "丢失目标时清除BOSS");
        add("terra_entity.configuration.boss_attributes_multiplier_health", "BOSS属性倍率-生命");
        add("terra_entity.configuration.boss_attributes_multiplier_damage", "BOSS属性倍率-伤害");
        add("terra_entity.configuration.boss_no_physics", "BOSS能否穿墙");
        add("terra_entity.configuration.boss_leave_on_day", "特定的BOSS是否在白天离开");


        add("terra_entity.configuration.display_summon_items", "在创造栏显示召唤物品");
        add("terra_entity.configuration.enhance_all_monster", "增强所有怪物");
        add("terra_entity.configuration.monster_attributes_multiplier_health", "Monster属性倍率-生命");
        add("terra_entity.configuration.monster_attributes_multiplier_damage", "Monster属性倍率-伤害");

        add("terra_entity.configuration.boss_bar_style", "BOSS血条样式");
        add("terra_entity.configuration.boss_bar_number_offset_x", "BOSS血条数字偏移-X");
        add("terra_entity.configuration.boss_bar_number_offset_y", "BOSS血条数字偏移-Y");
        add("terra_entity.configuration.enableNonSpiderModel", "蜘蛛和谐");
        add("terra_entity.configuration.enableNonSpiderModel.tooltip", "如果你对蜘蛛反感或想美化他们，请开启这个选项");

        add("terra_entity.configuration.generate_projectile_particle", "生成弹幕粒子");

        // Tooltip
        add("tooltip.terra_entity.summon_item_cost", "仆从占用: %s");
        add("tooltip.terra_entity.summon_item_entity", "仆从类型: %s");
        add("tooltip.terra_entity.summon_info", "仆从栏位: %d / %d");

        add("tooltip.terra_entity.whip.hit_effect", "命中效果: ");
        add("tooltip.terra_entity.whip.hit_effect_beneficial", "农场主的训斥");
        add("tooltip.terra_entity.house_detect.mode", "房屋工具模式:");
        add("tooltip.terra_entity.house_detect.mode.check", "探测");
        add("tooltip.terra_entity.house_detect.mode.check.owner", "所有者");
        add("tooltip.terra_entity.house_detect.mode.add", "添加");
        add("tooltip.terra_entity.house_detect.mode.add.failed", "添加房屋失败，房屋已存在!");
        add("tooltip.terra_entity.house_detect.mode.add.success", "添加房屋成功!");
        add("tooltip.terra_entity.house_detect.mode.delete", "删除");
        add("tooltip.terra_entity.house_detect.mode.delete.success", "删除房屋成功!");
        add("tooltip.terra_entity.house_detect.not_npc", "这不是npc!");
        add("tooltip.terra_entity.house_detect.no_detect", "使用前先探测房屋!");
        add("tooltip.terra_entity.house_detect.message.too_large", "这个房间太大了!");
        add("tooltip.terra_entity.house_detect.message.too_small", "这个房间太小了!");
        add("tooltip.terra_entity.house_detect.message.no_dynamic_light", "房间缺少光源!");
        add("tooltip.terra_entity.house_detect.message.found_house", "这个房间很合适!");
        add("tooltip.terra_entity.house_detect.info", "按下shift+右键切换模式");


            // boomerang
        add("tooltip.terra_entity.boomerang.penetration", "穿透数量");
        add("tooltip.terra_entity.boomerang.on_hit_effects", "命中效果");
        add("tooltip.terra_entity.boomerang.max_count", "分身数量");
        add("tooltip.terra_entity.boomerang.fly_speed", "飞行速度");

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

        // hit effect
        add("terra_entity.effect.strategy.mud", "泥潭");

        add("terra_entity.effect.strategy.bat", "吸血 +1 hp");
        add("terra_entity.effect.strategy.lights_bane", "召唤魔光剑");
        add("terra_entity.effect.strategy.bee_keeper", "召唤蜜蜂");

        add("terra_entity.effect.strategy.frozen_burn_3_sec_50_chance", "50%几率 霜冻 3秒");
        add("terra_entity.effect.strategy.tentacle_spikes", "触手钉锤");
        add("terra_entity.effect.strategy.hunting_4_sec", "狩猎 4秒");
        add("terra_entity.effect.strategy.hell_fire_5_sec", "烈火焚身 5秒");
        add("terra_entity.effect.strategy.set_fire_5_sec", "着火啦 5秒");
        add("terra_entity.effect.strategy.blood_butchered", "血腥屠宰");
        add("terra_entity.effect.strategy.bei_dou", "随机5种效果:\n     霜冻 Ⅳ 10秒\n     烈火焚身 Ⅳ 10秒\n     凋零 Ⅳ 10秒\n     中毒 Ⅳ 10秒\n     瞬间伤害 Ⅷ");

        add("terra_entity.effect.strategy.strength", "愤怒");

        // 车万女仆
        add("task.terra_entity.boomerang_attack", "回旋镖攻击");
        add("task.terra_entity.boomerang_attack.desc", "女仆会主动用回旋镖攻击周围的敌对生物");
        add("task.terra_entity.boomerang_attack.condition.has_boomerang", "主手持有回旋镖");

        // 附魔
        add("enchantment.terra_entity.multi_boomerang", "影分身");
        add("enchantment.terra_entity.whip_sweep", "横扫之鞭");

    }
}
