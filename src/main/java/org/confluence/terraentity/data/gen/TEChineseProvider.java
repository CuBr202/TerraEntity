package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TEItems;
import org.confluence.terraentity.init.entity.*;
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
        add("title.terra_entity.npc_trade.task.daily", "每日任务");
        add("title.terra_entity.npc_trade.task.fixed_level", "固定等级任务");
        add("title.terra_entity.npc_trade.task.random", "随机任务");
        add("title.terra_entity.npc_trade.task.dynamic_reward", "动态奖励任务");
        add("title.terra_entity.npc_trade.task.progress", "进度任务");

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
        add("entity.terra_entity.dungeon_slime", "地牢史莱姆");
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


        add(TEBossEntities.KING_SLIME.get(), "史莱姆王");
        add(TEBossEntities.EYE_OF_CTHULHU.get(), "克苏鲁之眼");
        add(TEBossEntities.EATER_OF_WORLDS.get(), "世界吞噬怪");
        add(TEBossEntities.EATER_OF_WORLDS_SEGMENT.get(), "世界吞噬怪体节");
        add(TEBossEntities.BRAIN_OF_CTHULHU.get(), "克苏鲁之脑");
        add(TEBossEntities.BRAIN_FAKE.get(), "克苏鲁之脑幻象");
        add(TEMonsterEntities.VISUAL_NEURON.get(), "视神经元");
        add(TEBossEntities.QUEEN_BEE.get(), "蜂王");
        add(TEMonsterEntities.LITTLE_HORNET.get(), "小黄蜂");
        add(TEBossEntities.SKELETRON.get(), "骷髅王");
        add(TEBossEntities.DUNGEON_GUARDIAN.get(), "地牢守卫");


        add(TESummonEntities.SUMMON_SLIME.get(), "史莱姆宝宝");
        add(TESummonEntities.SUMMON_IRON_GOLEM.get(), "i-32型铁傀儡");
        add(TESummonEntities.SUMMON_HORNET.get(), "仆役黄蜂");

        add(TERideableEntities.RIDEABLE_SLIME.get(), "史莱姆坐骑");
        add(TERideableEntities.RIDEABLE_BEE.get(), "蜜蜂坐骑");


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
        add(TESpawnEggItems.DUNGEON_SLIME_SPAWN_EGG.get(), "地牢史莱姆刷怪蛋");
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
        add(TESpawnEggItems.SKELETRON_SPAWN_EGG.get(), "骷髅王刷怪蛋");
        add(TESpawnEggItems.DUNGEON_GUARDIAN_SPAWN_EGG.get(), "地牢守卫刷怪蛋");



        add(TENpcEntities.GUIDE.get(), "向导");
        add(TENpcEntities.DEMOLITIONIST.get(), "爆破专家");
        add(TENpcEntities.GOBLIN_TINKERER.get(), "哥布林工匠");
        add(TENpcEntities.ARMS_DEALER.get(), "军火商");
        add(TENpcEntities.NURSE.get(), "护士");
        add(TENpcEntities.MERCHANT.get(), "商人");
        add(TENpcEntities.PAINTER.get(), "油漆工");
        add(TENpcEntities.DRYAD.get(), "树妖");
        add(TENpcEntities.DYE_TRADER.get(), "染料商");
        add(TENpcEntities.ANGLER.get(), "渔夫");
        add(TENpcEntities.OLD_MAN.get(), "老人");



        add(TESpawnEggItems.GUILD_SPAWN_EGG.get(), "向导刷怪蛋");
        add(TESpawnEggItems.DEMOLITIONIST_SPAWN_EGG.get(), "爆破专家刷怪蛋");
        add(TESpawnEggItems.GOBLIN_TINKERER_SPAWN_EGG.get(), "哥布林工匠刷怪蛋");
        add(TESpawnEggItems.ARMS_DEALER_SPAWN_EGG.get(), "军火商刷怪蛋");
        add(TESpawnEggItems.NURSE_SPAWN_EGG.get(), "护士刷怪蛋");
        add(TESpawnEggItems.MERCHANT_SPAWN_EGG.get(), "商人刷怪蛋");
        add(TESpawnEggItems.PAINTER_SPAWN_EGG.get(), "油漆工刷怪蛋");
        add(TESpawnEggItems.DRYAD_SPAWN_EGG.get(), "树妖刷怪蛋");
        add(TESpawnEggItems.DYE_TRADER_SPAWN_EGG.get(), "染料商刷怪蛋");
        add(TESpawnEggItems.ANGLER_SPAWN_EGG.get(), "渔夫刷怪蛋");
        add(TESpawnEggItems.OLD_MAN_SPAWN_EGG.get(), "老人刷怪蛋");


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

        // npc对话
        add("dialogs.terra_entity.guide.0", "我的工作是为你接下来的任务提供建议。建议你遇到任何困难时都来和我谈谈。");
        add("dialogs.terra_entity.guide.1", "他们说，有个人会告诉你如何在这地方上生存……哦等下。那个人就是我。");
        add("dialogs.terra_entity.guide.2", "晚上你应该呆在家里。黑夜在外面转悠非常危险。");
        add("dialogs.terra_entity.guide.3", "在融合的世界中，你会收获多倍的宝藏，但这也以为着承担多倍的风险。");
        add("dialogs.terra_entity.guide.4", "据我所知这个世界上的人类比我们原来的世界更多。");
        add("dialogs.terra_entity.guide.5", "抱歉，有时候我不得不开门。");
        add("dialogs.terra_entity.guide.6", "那些会爆炸的家伙比一般的地表怪物更具威胁！");
        add("dialogs.terra_entity.guide.7", "草地上的生命蘑菇有时候可以救你一命。");
        add("dialogs.terra_entity.guide.8", "地下有水晶之心，可以用来提高你的最大生命值。你可以用镐来打碎它们。");
        add("dialogs.terra_entity.guide.9", "地底下有一种具有神奇魔力的湖，它非常稀有。");
        add("dialogs.terra_entity.guide.10", "夜晚，星星在坠落，洒满全世界。它们的用途极为广泛。如果你看到了，一定要拿到手，因为星星在日出后就会消失。");
        add("dialogs.terra_entity.guide.11", "无论是什么东西在疯狂蔓延，你都将意识到是时候阻止它们。");
        add("dialogs.terra_entity.guide.12", "如果你想活下来，你需要制造武器和建造房屋。首先要砍树并收集木材。");
        add("dialogs.terra_entity.guide.13", "拥有一把剑后，你可以试试从史莱姆身上收集一些凝胶。用木棍和凝胶制作火把！");
        add("dialogs.terra_entity.guide.14", "如果你拥有了一些矿石，你需要将它铸成矿锭，才能用来制作物品。这需要熔炉！");
        add("dialogs.terra_entity.guide.15", "如果在祭坛上合成晶状体，你也许能够找到方法来召唤一个强大的怪物。不过，最好等到夜晚再用它。");


        add("dialogs.terra_entity.nurse.0", "我要和向导认真谈一谈。你一周到底有多少次被熔岩烫成重伤？");
        add("dialogs.terra_entity.nurse.1", "看到那个在地牢周围转来转去的老人没？他看上去遇到麻烦了。");
        add("dialogs.terra_entity.nurse.2", "嗨，军火商有没有提过要去看医生啥的？就随便问问。");
        add("dialogs.terra_entity.nurse.3", "又惹上混混了？");
        add("dialogs.terra_entity.nurse.4", "别像个孩子似的！我见过更糟的。");
        add("dialogs.terra_entity.nurse.5", "你这么做的时候疼吗？别那么做。");

        add("dialogs.terra_entity.demolitionist.0", "炸药如今十分火爆。马上买一些！");
        add("dialogs.terra_entity.demolitionist.1", "今天是个找死的好日子！");
        add("dialogs.terra_entity.demolitionist.2", "让我看看这样会怎……（轰！）……哦，对不起，你还要那条腿吗？");
        add("dialogs.terra_entity.demolitionist.3", "看看我的商品；都是惊爆价");
        add("dialogs.terra_entity.demolitionist.4", "雷管，这是我特别为你准备的灵丹妙药，包治百病。");
        add("dialogs.terra_entity.demolitionist.5", "想穿过那些邪恶石头，嗯？为什么不用炸药炸掉它！");

        add("dialogs.terra_entity.goblin_tinkerer.0", "哥布林太容易生气了。事实上，他们能为了一些破布发动战争！");
        add("dialogs.terra_entity.goblin_tinkerer.1", "老实说，大部分哥布林都不是真正的火箭科学家。好吧，有一些是。");
        add("dialogs.terra_entity.goblin_tinkerer.2", "你知不知道为什么大家到哪儿都带着这些尖刺球？因为我不知道。");
        add("dialogs.terra_entity.goblin_tinkerer.3", "我刚刚完成了最新的作品！这个版本就算你对着它猛力吹吸也不会猛烈爆炸。");
        add("dialogs.terra_entity.goblin_tinkerer.4", "哥布林盗贼不太擅长偷东西。没上锁的箱子都不会偷！");
        add("dialogs.terra_entity.goblin_tinkerer.5", "唷，我听说你喜欢火箭和跑鞋，所以我在你的跑鞋上加了一些火箭。");

        add("dialogs.terra_entity.arms_dealer.0", "哥们，把手从我的枪上拿开！");
        add("dialogs.terra_entity.arms_dealer.1", "嘿，兄弟，这可不是演电影。需要另行准备弹药。");
        add("dialogs.terra_entity.arms_dealer.2", "我看你在盯着迷你鲨……你绝对想不到它是怎么做成的。");
        add("dialogs.terra_entity.arms_dealer.3", "我想买护士卖的东西。你说啥？她什么也不卖？");
        add("dialogs.terra_entity.arms_dealer.4", "飞鱼？我把它叫作打靶！");
        add("dialogs.terra_entity.arms_dealer.5", "别和爆破专家浪费时间了。我这边有你要的一切。");

        add("dialogs.terra_entity.merchant.0", "剑克纸！赶紧买一把。");
        add("dialogs.terra_entity.merchant.1", "你想要苹果？你想要胡萝卜？你想要菠萝？我们只有火把。");
        add("dialogs.terra_entity.merchant.2", "看看我的土块；它们特别土。");
        add("dialogs.terra_entity.merchant.3", "你是不知道土块能在国外卖多少钱。");
        add("dialogs.terra_entity.merchant.4", "总有一天他们会讲述你的传奇……肯定会是好故事。");
        add("dialogs.terra_entity.merchant.5", "Kosh, kapleck Mog。哦，对不起，这是克林贡语，意思是“要么买，要么死。");

        add("dialogs.terra_entity.painter.0", "我知道青绿色和蓝绿色之间的差别。但我不会告诉你。");
        add("dialogs.terra_entity.painter.1", "钛白色用完了，别问了。");
        add("dialogs.terra_entity.painter.2", "尝试调合粉色和紫色，肯定管用，我发誓！");
        add("dialogs.terra_entity.painter.3", "不、不、不……灰色也分很多种！别让我开始……");
        add("dialogs.terra_entity.painter.4", "我希望别下雨了，漆还没干。下雨就惨了！");
        add("dialogs.terra_entity.painter.5", "我试过举办一次彩弹大战，但是每个人都只想要食物和装饰品。");

        add("dialogs.terra_entity.dryad.0", "注意安全！两边的世界都需要你！");
        add("dialogs.terra_entity.dryad.1", "时间的沙漏在缓缓流逝。而你并没有优雅地变老。");
        add("dialogs.terra_entity.dryad.2", "两个哥布林走进酒吧，其中一个对另一个说：“来杯啤酒？！");
        add("dialogs.terra_entity.dryad.3", "说我雷声大雨点小是啥意思？");
        add("dialogs.terra_entity.dryad.4", "你必须停止邪恶的蔓延。");
        add("dialogs.terra_entity.dryad.5", "这个世界更为广阔……自然的力量也更强大了");

        add("dialogs.terra_entity.dye_trader.0", "我带给你最丰富的色彩，以换取你的财富。");
        add("dialogs.terra_entity.dye_trader.1", "亲爱的，你的穿着太单调了。你一定得好好学学，怎么给单调的衣服染色！");
        add("dialogs.terra_entity.dye_trader.2", "我唯一愿意染的木材是红木。给任何其他木材染色都是浪费。");
        add("dialogs.terra_entity.dye_trader.3", "噢，不行，不行，这样是不行的。有钱也没用，你必须拿稀有的植物样本来和我交换！");
        add("dialogs.terra_entity.dye_trader.4", "这些染料瓶？抱歉，亲爱的朋友，这些是非卖品。我只接受用最珍稀的植物来交换它们！");
        add("dialogs.terra_entity.dye_trader.5", "你以为可以骗过我的眼睛？我可不这么想！我只接受用最稀有的花来交换这些特别的瓶子。");

        add("dialogs.terra_entity.angler.0", "谢谢，我想，谢谢你救了我之类的。你是个优秀的得力仆从！");
        add("dialogs.terra_entity.angler.1", "啥？你是哪位？我绝对不是溺水之类的！");
        add("dialogs.terra_entity.angler.2", "你救了我！你太好了，我可以使唤你……呃，我是说，雇你帮我做些了不起的事！");
        add("dialogs.terra_entity.angler.3", "我没有妈妈，也没有爸爸，但我有很多鱼！这就够了！");
        add("dialogs.terra_entity.angler.4", "嘿！当心！我设了许多陷阱，用来实施史上最大的恶作剧！没人会发觉！你敢告诉别人试试！");
        add("dialogs.terra_entity.angler.5", "听说过会叫的鱼吗？！我没听说过，只是想知道你听说过没！");

        add("goblin_tinkerer_like_dye_trader", "染料商知道把东西混在一起是多么有趣，我能理解！");
        add("guide_hate_painter", "我讨厌油漆工在附近。世界本来挺美好的！");
        add("arms_dealer_hate_demolitionist", "爆破专家怎么回事啊？难道他没发现我们卖的东西完全不同？");
        add("arms_dealer_love_nurse", "那啥，你觉得护士对我有意思吗");
        add("angler_like_demolitionist", "爆破专家其实知道他们在做什么，不像某些其他人！我挺喜欢的！");
        add("dye_trader_like_arms_dealer", "军火商善于发现鲜艳的颜色和商机，对吧？我喜欢。");
        add("dye_trader_like_painter", "油漆工善于发现鲜艳的颜色和商机，对吧？我喜欢。”");
        add("demolitionist_dislike_arms_dealer", "我想把军火商绑到火箭上，看看会发生什么！");
        add("demolitionist_dislike_goblin_tinkerer", "我想把哥布林工匠绑到火箭上，看看会发生什么！");
        add("painter_love_dryad", "我真的很想画树妖……当然是因为色彩鲜艳！");
        add("dryad_dislike_angler", "我不喜欢渔夫不尊重其他生物。");
        add("merchant_like_nurse", "护士赚了很多钱，我喜欢有钱人。");
        add("nurse_love_arms_dealer", "什么？军火商？我才没有暗恋他！我没有！闭嘴！");
        add("nurse_dislike_dryad", "我不太喜欢树妖，这个人有点奇怪。");

    }
}
