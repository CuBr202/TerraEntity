package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TEItems;

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

        Consumer<DeferredHolder<Item, ? extends Item>> itemAction = item -> add(item.get(), toTitleCase(item.getId().getPath()));
        TEItems.SPAWN_EGGS.getEntries().forEach(itemAction);
        TEItems.SUMMON_ITEMS.getEntries().forEach(itemAction);
        TEEntities.ENTITIES.getEntries().forEach(entity -> add(entity.get(), toTitleCase(entity.getId().getPath())));
        TEEffects.EFFECTS.getEntries().forEach(effect -> add(effect.get(), toTitleCase(effect.getId().getPath())));

        add("itemGroup.terraentity.title", "Terra Entity");


        add("message.terraentity.boss_spawn", "%s Has Awoken!");
        add("message.terraentity.boss_leave", "%s Have Been Defeated!");
        add("message.terraentity.boss_discard", "Has Been Discarded！");

        // Config
        add("terra_entity.configuration.boss_clear_when_no_target", "Clear Boss When No Target");
        add("terra_entity.configuration.boss_attributes_multiplier_health", "Boss Attributes Multiplier-Health");
        add("terra_entity.configuration.boss_attributes_multiplier_damage", "Boss Attributes Multiplier-Damage");
        add("terra_entity.configuration.boss_no_physics", "BOSS have no physics");
        add("terra_entity.configuration.boss_leave_on_day", "Specify BOSS Leave on Day");


        add("terra_entity.configuration.display_summon_items", "Display Summon Items In Creative Tab");
        add("terra_entity.configuration.enhance_all_monster", "Enhance All Monster");
        add("terra_entity.configuration.monster_attributes_multiplier_health", "Monster Attributes Multiplier-Health");
        add("terra_entity.configuration.monster_attributes_multiplier_damage", "Monster Attributes Multiplier-Damage");

        add("terra_entity.configuration.boss_bar_style", "Boss Health Bar Style");
        add("terra_entity.configuration.boss_bar_number_offset_x", "Boss Health Bar Number Offset-X");
        add("terra_entity.configuration.boss_bar_number_offset_y", "Boss Health Bar Number Offset-Y");



        add("terra_entity.configuration.enableNonSpiderModel", "Spider Harmonization");
        add("terra_entity.configuration.enableNonSpiderModel.tooltip", "Enable this option if you dislike spiders or want to beautify them.");


        // Tooltip
        add("tooltip.terra_entity.summon_item_cost", "Summon Cost: %d");
        add("tooltip.terra_entity.summon_item_entity", "Summon Entity: %s");
        add("tooltip.terra_entity.summon_info", "Summon Info: %d / %d");

        // attribute
        add("attribute.name.player.summon_damage", "Summon Damage");

        // track
        add("terra_entity.track_type.simple", "Simple Track");
        add("terra_entity.track_type.basis", "Basis Track");

    }
}
