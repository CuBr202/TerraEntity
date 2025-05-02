package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.item.TEBoomerangItems;
import org.confluence.terraentity.init.item.TESpawnEggItems;
import org.confluence.terraentity.init.item.TESummonItems;
import org.confluence.terraentity.init.item.TEWhipItems;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEEnglishProvider extends LanguageProvider {
    public TEEnglishProvider(PackOutput output) {
        super(output, MODID, "en_us");
    }

    private static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    @Override
    protected void addTranslations() {

        Consumer<RegistryObject<Item>> itemAction = item -> add(item.get(), toTitleCase(item.getId().getPath()));
        TESpawnEggItems.ITEMS.getEntries().forEach(itemAction);
        TESummonItems.ITEMS.getEntries().forEach(itemAction);
        TEWhipItems.ITEMS.getEntries().forEach(itemAction);
        TEEntities.ENTITIES.getEntries().forEach(entity -> add(entity.get(), toTitleCase(entity.getId().getPath())));
        TEEffects.EFFECTS.getEntries().forEach(effect -> add(effect.get(), toTitleCase(effect.getId().getPath())));
        TEBoomerangItems.ITEMS.getEntries().forEach(itemAction);

        add("itemGroup.terraentity.title", "Terra Entity");

        add("title.terra_entity.npc_trade", "Terra Shop");
        add("title.terra_entity.npc_trade.task.daily", "Daily Task");
        add("title.terra_entity.npc_trade.task.fixed_level", "Fixed Level Task");
        add("title.terra_entity.npc_trade.task.random", "Random Task");
        add("title.terra_entity.npc_trade.task.dynamic_reward", "Dynamic Reward Task");
        add("title.terra_entity.npc_trade.task.progress", "Progress Task");



        add("entity.terra_entity.mother_slime", "Mother Slime");
        add("entity.terra_entity.baby_slime", "Baby Slime");

        add("message.terraentity.boss_spawn", "%s Has Awoken!");
        add("message.terraentity.boss_leave", "%s Have Been Defeated!");
        add("message.terraentity.boss_discard", "Has Been Discarded！");
        add("message.terra_entity.trade.not_enough_items", "Not Enough Items");


        add("terra_entity.options.title", "Terra Entity Options");
        add("terra_entity.configuration.server", "Server Configuration");
        add("terra_entity.configuration.client", "Client Configuration");

        // Config
        add("terra_entity.configuration.server.boss_clear_when_no_target", "Clear Boss When No Target");
        add("terra_entity.configuration.server.boss_attributes_multiplier_health", "Boss Attributes Multiplier-Health");
        add("terra_entity.configuration.server.boss_attributes_multiplier_damage", "Boss Attributes Multiplier-Damage");
        add("terra_entity.configuration.server.boss_no_physics", "BOSS have no physics");
        add("terra_entity.configuration.server.boss_leave_on_day", "Specify BOSS Leave on Day");


        add("terra_entity.configuration.server.display_summon_items", "Display Summon Items In Creative Tab");
        add("terra_entity.configuration.server.enhance_all_monster", "Enhance All Monster");
        add("terra_entity.configuration.server.monster_attributes_multiplier_health", "Monster Attributes Multiplier-Health");
        add("terra_entity.configuration.server.monster_attributes_multiplier_damage", "Monster Attributes Multiplier-Damage");

        add("terra_entity.configuration.client.boss_bar_style", "Boss Health Bar Style");
        add("terra_entity.configuration.client.boss_bar_number_offset_x", "Boss Health Bar Number Offset-X");
        add("terra_entity.configuration.client.boss_bar_number_offset_y", "Boss Health Bar Number Offset-Y");
        add("terra_entity.configuration.client.generate_projectile_particle", "Generate Projectile Particle");


        add("terra_entity.configuration.client.enableNonSpiderModel", "Spider Harmonization");
        add("terra_entity.configuration.client.enableNonSpiderModel.tooltip", "Enable this option if you dislike spiders or want to beautify them.");


        // Tooltip
        add("tooltip.terra_entity.summon_item_cost", "Summon Cost: %d");
        add("tooltip.terra_entity.summon_item_entity", "Summon Entity: %s");
        add("tooltip.terra_entity.summon_info", "Summon Info: %d / %d");
        add("tooltip.terra_entity.whip.hit_effect", "Hit Effect:");
        add("tooltip.terra_entity.whip.hit_effect_beneficial", "Farmer's Flogging");
        add("tooltip.terra_entity.house_detect.mode", "House Detect Mode: ");
        add("tooltip.terra_entity.house_detect.mode.check", "Check");
        add("tooltip.terra_entity.house_detect.mode.check.owner", "Owner");
        add("tooltip.terra_entity.house_detect.mode.add", "Add");
        add("tooltip.terra_entity.house_detect.mode.add.failed", "Add House failed. House exists.");
        add("tooltip.terra_entity.house_detect.mode.add.success", "Add House success.");
        add("tooltip.terra_entity.house_detect.mode.delete", "Delete");
        add("tooltip.terra_entity.house_detect.mode.delete.success", "Delete House success.");
        add("tooltip.terra_entity.house_detect.not_npc", "You should point at an NPC.");
        add("tooltip.terra_entity.house_detect.no_detect", "You should check before that.");
        add("tooltip.terra_entity.house_detect.message.too_large", "House Too Large");
        add("tooltip.terra_entity.house_detect.message.too_small", "House Too Small");
        add("tooltip.terra_entity.house_detect.message.no_dynamic_light", "House No Dynamic Light");
        add("tooltip.terra_entity.house_detect.message.found_house", "Found House");
        add("tooltip.terra_entity.house_detect.info", "Press shift and right click to switch mode.");




            // boomerang
        add("tooltip.terra_entity.boomerang.penetration", "Penetrates Count");
        add("tooltip.terra_entity.boomerang.on_hit_effects", "Effects");
        add("tooltip.terra_entity.boomerang.max_count", "Max Count");
        add("tooltip.terra_entity.boomerang.fly_speed", "Fly Speed");

        // attribute
        add("attribute.name.player.summon_damage", "Summon Damage");
        add("attribute.name.player.mark_damage", "Mark Damage");
        add("attribute.name.player.whip_range", "Whip Range");
        add("attribute.name.player.summon_knockback", "Summon Knockback");
        add("attribute.name.player.minion_capacity", "Minion Capacity");
        add("attribute.name.player.sentry_capacity", "Sentry Capacity");

        // track
        add("terra_entity.track_type.simple", "Simple Track");
        add("terra_entity.track_type.basis", "Basis Track");

        // hit effect
        add("terra_entity.effect.strategy.mud", "Mud");
        add("terra_entity.effect.strategy.bat", "Blood absorb +1 hp");
        add("terra_entity.effect.strategy.lights_bane", "Summon lights bane");
        add("terra_entity.effect.strategy.bee_keeper", "Summon bees");


        add("terra_entity.effect.strategy.frozen_burn_3_sec_50_chance", "50% chance frozen burn 3 seconds");
        add("terra_entity.effect.strategy.tentacle_spikes", "Tentacle spikes");
        add("terra_entity.effect.strategy.hunting_4_sec", "Hunting 4 seconds");
        add("terra_entity.effect.strategy.hell_fire_5_sec", "Hell fire 5 seconds");
        add("terra_entity.effect.strategy.set_fire_5_sec", "Set fire 5 seconds");
        add("terra_entity.effect.strategy.blood_butchered", "Blood Butchered");
        add("terra_entity.effect.strategy.bei_dou", "Random 5 effects:\\nfrozen burn ⅳ 10 seconds\\nhell fire ⅳ 10 seconds\\nwither ⅳ 10 seconds\\npoison ⅳ 10 seconds\\ninstant harm ⅷ");

        add("terra_entity.effect.strategy.strength", "Anger");


        // TouhouLittleMaid
        add("task.terra_entity.boomerang_attack", "Boomerang Attack");
        add("task.terra_entity.boomerang_attack.desc", "Maid attacks enemies with a boomerang.");
        add("task.terra_entity.boomerang_attack.condition.has_boomerang", "Mainhand holds a boomerang");

        // 附魔
        add("enchantment.terra_entity.multi_boomerang", "Multi Shoot Boomerang");
        add("enchantment.terra_entity.whip_sweep", "Whip Sweep");


        // npc
        add("dialogs.terra_entity.guide.0", "My job is to offer suggestions for your upcoming tasks. I recommend that you come and talk to me whenever you encounter any difficulties.");
        add("dialogs.terra_entity.guide.1", "They said there would be someone to tell you how to survive in this place... Oh, wait a moment. That person is me.");
        add("dialogs.terra_entity.guide.2", "You should stay at home at night. It's very dangerous to wander outside in the dark.");
        add("dialogs.terra_entity.guide.3", "In the Confluence world, you will obtain multiple times the treasure, but this also means taking on multiple times the risk.");
        add("dialogs.terra_entity.guide.4", "As far as I know, there are more humans in this world than in our original world.");
        add("dialogs.terra_entity.guide.5", "Sorry, sometimes I have to open the door.");
        add("dialogs.terra_entity.guide.6", "Those guys that can explode are more threatening than the average surface monsters!");
        add("dialogs.terra_entity.guide.7", "The life mushrooms on the grass can sometimes save your life.");
        add("dialogs.terra_entity.guide.8", "There are Crystal Hearts underground, which can be used to increase your maximum health. You can use a pickaxe to break them.");
        add("dialogs.terra_entity.guide.9", "There is a lake with magical powers underground, and it's very rare.");
        add("dialogs.terra_entity.guide.10", "At night, stars are falling and spreading all over the world. They have extremely wide uses. If you see them, you must get them, because the stars will disappear after sunrise.");
        add("dialogs.terra_entity.guide.11", "No matter what is spreading wildly, you will realize that it's time to stop them.");
        add("dialogs.terra_entity.guide.12", "If you want to survive, you need to make weapons and build a house. First, cut down trees and collect wood.");
        add("dialogs.terra_entity.guide.13", "After you have a sword, you can try to collect some gel from slimes. Use wooden sticks and gel to make torches!");
        add("dialogs.terra_entity.guide.14", "If you have some ores, you need to smelt them into ingots before you can use them to make items. This requires a furnace!");
        add("dialogs.terra_entity.guide.15", "If you combine lenses on the altar, you may be able to find a way to summon a powerful monster. However, it's better to use it at night.");

        add("dialogs.terra_entity.nurse.0", "I need to have a serious talk with the Guide. How many times a week do you get severely burned by lava exactly?");
        add("dialogs.terra_entity.nurse.1", "See that old man wandering around the dungeon? He looks like he's in trouble.");
        add("dialogs.terra_entity.nurse.2", "Hey, has the Arms Dealer ever mentioned going to see a doctor or something? Just asking.");
        add("dialogs.terra_entity.nurse.3", "Got into trouble with the thugs again?");
        add("dialogs.terra_entity.nurse.4", "Don't be such a child! I've seen worse.");
        add("dialogs.terra_entity.nurse.5", "Did it hurt when you did that? Don't do that.");

        add("dialogs.terra_entity.demolitionist.0", "Explosives are really popular nowadays. Buy some right away!");
        add("dialogs.terra_entity.demolitionist.1", "Today is a great day to court death!");
        add("dialogs.terra_entity.demolitionist.2", "Let me see what happens if I do this... (BOOM!)... Oh, sorry, did you still need that leg?");
        add("dialogs.terra_entity.demolitionist.3", "Take a look at my goods; they're all at amazing prices.");
        add("dialogs.terra_entity.demolitionist.4", "Dynamite, this is my special panacea prepared just for you. It can cure all kinds of problems.");
        add("dialogs.terra_entity.demolitionist.5", "Want to get through those evil stones, huh? Why not just blow them up with explosives!");

        add("dialogs.terra_entity.goblin_tinkerer.0", "Goblins get angry so easily. In fact, they can start a war over some rags!");
        add("dialogs.terra_entity.goblin_tinkerer.1", "To be honest, most goblins aren't real rocket scientists. Well, some of them are.");
        add("dialogs.terra_entity.goblin_tinkerer.2", "Do you know why everyone carries these spiky balls around? Because I don't.");
        add("dialogs.terra_entity.goblin_tinkerer.3", "I've just finished my latest creation! This version won't explode violently even if you blow or suck on it really hard.");
        add("dialogs.terra_entity.goblin_tinkerer.4", "Goblin thieves aren't very good at stealing. They can't even steal from an unlocked chest!");
        add("dialogs.terra_entity.goblin_tinkerer.5", "Yo, I heard you like rockets and running shoes, so I added some rockets to your running shoes.");

        add("dialogs.terra_entity.arms_dealer.0", "Dude, get your hands off my gun!");
        add("dialogs.terra_entity.arms_dealer.1", "Hey, bro, this isn't a movie. You need to prepare ammunition separately.");
        add("dialogs.terra_entity.arms_dealer.2", "I see you're eyeing the Minishark... You can't even imagine how it's made.");
        add("dialogs.terra_entity.arms_dealer.3", "I want to buy something from the Nurse. What did you say? She doesn't sell anything?");
        add("dialogs.terra_entity.arms_dealer.4", "Flying Fish? I call it target practice!");
        add("dialogs.terra_entity.arms_dealer.5", "Don't waste your time with the Demolitionist. I've got everything you need right here.");

        add("dialogs.terra_entity.merchant.0", "Swords beat paper! Buy one right away.");
        add("dialogs.terra_entity.merchant.1", "Do you want apples? Do you want carrots? Do you want pineapples? All we have are torches.");
        add("dialogs.terra_entity.merchant.2", "Take a look at my dirt blocks; they're really earthy.");
        add("dialogs.terra_entity.merchant.3", "You have no idea how much dirt blocks can sell for in other places.");
        add("dialogs.terra_entity.merchant.4", "One day they will tell your legend... It's sure to be a good story.");
        add("dialogs.terra_entity.merchant.5", "Kosh, kapleck Mog. Oh, sorry, that's Klingon, which means 'Buy or die.'");

        add("dialogs.terra_entity.painter.0", "I know the difference between turquoise and teal. But I'm not going to tell you.");
        add("dialogs.terra_entity.painter.1", "The titanium white is all used up. Don't ask.");
        add("dialogs.terra_entity.painter.2", "Try mixing pink and purple. It'll definitely work, I swear!");
        add("dialogs.terra_entity.painter.3", "No, no, no... There are many kinds of gray! Don't make me start...");
        add("dialogs.terra_entity.painter.4", "I hope it stops raining. The paint still hasn't dried. It would be a disaster if it rains!");
        add("dialogs.terra_entity.painter.5", "I tried organizing a paintball war, but everyone just wanted food and decorations.");

        add("dialogs.terra_entity.dryad.0", "Stay safe! Both worlds need you!");
        add("dialogs.terra_entity.dryad.1", "The hourglass of time is slowly running out. And you're not aging gracefully.");
        add("dialogs.terra_entity.dryad.2", "Two goblins walked into a bar, and one of them said to the other: 'A glass of beer?'");
        add("dialogs.terra_entity.dryad.3", "What does it mean by saying I'm all talk and no action?");
        add("dialogs.terra_entity.dryad.4", "You must stop the spread of evil.");
        add("dialogs.terra_entity.dryad.5", "This world is much vaster... And the power of nature is stronger too.");

        add("dialogs.terra_entity.dye_trader.0", "I bring you the richest colors in exchange for your wealth.");
        add("dialogs.terra_entity.dye_trader.1", "Honey, your clothes are so monotonous. You really have to learn how to dye your dull clothes!");
        add("dialogs.terra_entity.dye_trader.2", "The only wood I'm willing to dye is mahogany. Dyeing any other wood is a waste.");
        add("dialogs.terra_entity.dye_trader.3", "Oh, no, no, that won't do. Even if you have money, you have to trade me with rare plant samples!");
        add("dialogs.terra_entity.dye_trader.4", "These dye bottles? Sorry, my dear friend, these are not for sale. I only accept the rarest plants in exchange for them!");
        add("dialogs.terra_entity.dye_trader.5", "You think you can fool my eyes? I don't think so! I only accept the rarest flowers in exchange for these special bottles.");

        add("dialogs.terra_entity.angler.0", "Thanks, I guess, for saving me and all that. You're an excellent lackey!");
        add("dialogs.terra_entity.angler.1", "What? Who are you? I definitely wasn't drowning or anything!");
        add("dialogs.terra_entity.angler.2", "You saved me! You're so kind. I can boss you around... Uh, I mean, hire you to do some amazing things for me!");
        add("dialogs.terra_entity.angler.3", "I don't have a mom or a dad, but I have a lot of fish! That's enough!");
        add("dialogs.terra_entity.angler.4", "Hey! Watch out! I've set a lot of traps for the greatest prank in history! No one will notice! Try telling anyone and see what happens!");
        add("dialogs.terra_entity.angler.5", "Have you ever heard of a fish that can make noise?! I haven't. I just want to know if you have!");

        add("mood.info.goblin_tinkerer_like_dye_trader", "Dye Trader understands how fun it is to mix things together, I can respect that!");
        add("mood.info.guide_hate_painter", "I hate that Painter is around. The world is fine the way it was made!");
        add("mood.info.arms_dealer_hate_demolitionist", "I'd REALLY like to use the Demolitionist as a range target sometime.");
        add("mood.info.arms_dealer_love_nurse", "Think Nurse the Nurse ever, ya know, checks me out?");
        add("mood.info.angler_like_demolitionist", "the Demolitionist actually knows what they're doing, unlike some OTHER people! I kinda like that!");
        add("mood.info.dye_trader_like_arms_dealer", "Arms Dealer has good eyes for vividness and business, I like it, yes?");
        add("mood.info.dye_trader_like_painter", "Painter has good eyes for vividness and business, I like it, yes?");
        add("mood.info.demolitionist_dislike_arms_dealer", "I wanna strap Arms Dealer to a rocket and watch what happens!");
        add("mood.info.demolitionist_dislike_goblin_tinkerer", "I wanna strap Goblin Tinkerer to a rocket and watch what happens!");
        add("mood.info.painter_love_dryad", "I would really love to paint Dryad... because of the vivid colors, of course!");
        add("mood.info.dryad_dislike_angler", "I don't like that Angler has no respect for other beings.");
        add("mood.info.merchant_like_nurse", "Nurse makes loads of money, I like deep pockets.");
        add("mood.info.nurse_love_arms_dealer", "What? Arms Dealer? I don't have a crush! I don't! Shut up!");
        add("mood.info.nurse_dislike_dryad", "I don't like Dryad that much, kinda weirds me out.");
    }
}
