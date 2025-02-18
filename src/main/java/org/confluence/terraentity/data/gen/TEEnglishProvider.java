package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TEItems;

import java.util.Arrays;
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

        TEItems.SPAWN_EGGS.getEntries().forEach(entity -> add(entity.get(), toTitleCase(entity.getId().getPath())));
        TEEntities.ENTITIES.getEntries().forEach(entity -> add(entity.get(), toTitleCase(entity.getId().getPath())));
        TEEffects.EFFECTS.getEntries().forEach(effect -> add(effect.get(), toTitleCase(effect.getId().getPath())));

        add("itemGroup.terraentity.title", "Terra Entity");


        add("message.terraentity.boss_spawn", "%s Has Awoken!");
        add("message.terraentity.boss_leave", "%s Have Been Defeated!");
        add("message.terraentity.boss_discard", "Has Been Discarded！");

        // Config
        add("terra_entity.options.title", "Terra Entity Options");

        add("terra_entity.configuration.server.boss_clear_when_no_target", "Clear Boss When No Target");
        add("terra_entity.configuration.server.boss_attributes_multiplier_health", "Boss Attributes Multiplier-Health");
        add("terra_entity.configuration.server.boss_attributes_multiplier_damage", "Boss Attributes Multiplier-Damage");
        add("terra_entity.configuration.server.display_summon_items", "Display Summon Items In Creative Tab");
        add("terra_entity.configuration.server.enhance_all_monster", "Enhance All Monster");
        add("terra_entity.configuration.server.monster_attributes_multiplier_health", "Monster Attributes Multiplier-Health");
        add("terra_entity.configuration.server.monster_attributes_multiplier_damage", "Monster Attributes Multiplier-Damage");

        add("terra_entity.configuration.client.boss_bar_style", "Boss Health Bar Style");


        // Tooltip
        add("tooltip.terra_entity.summon_item_cost", "Summon Cost: %d");
        add("tooltip.terra_entity.summon_item_entity", "Summon Entity: %s");
        add("tooltip.terra_entity.summon_info", "Summon Info: %d / %d");

        // attribute
        add("attribute.name.player.summon_damage", "Summon Damage");

    }
}
