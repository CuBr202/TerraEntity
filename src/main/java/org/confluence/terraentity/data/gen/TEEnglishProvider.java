package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TEItems;
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

        add("entity.terra_entity.mother_slime", "Mother Slime");
        add("entity.terra_entity.baby_slime", "Baby Slime");

        add("message.terraentity.boss_spawn", "%s Has Awoken!");
        add("message.terraentity.boss_leave", "%s Have Been Defeated!");
        add("message.terraentity.boss_discard", "Has Been Discarded！");


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
        add("terra_entity.configuration.client.generate_whip_particle", "Generate Whip Particle");



        add("terra_entity.configuration.client.enableNonSpiderModel", "Spider Harmonization");
        add("terra_entity.configuration.client.enableNonSpiderModel.tooltip", "Enable this option if you dislike spiders or want to beautify them.");


        // Tooltip
        add("tooltip.terra_entity.summon_item_cost", "Summon Cost: %d");
        add("tooltip.terra_entity.summon_item_entity", "Summon Entity: %s");
        add("tooltip.terra_entity.summon_info", "Summon Info: %d / %d");
        add("tooltip.terra_entity.whip.hit_effect", "Hit Effect:");
        add("tooltip.terra_entity.whip.hit_effect_beneficial", "Farmer's Flogging");

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

    }
}
