package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terraentity.init.ModEffects;
import org.confluence.terraentity.init.ModItems;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.confluence.terraentity.TerraEntity.MODID;

public class ModEnglishProvider extends LanguageProvider {
    public ModEnglishProvider(PackOutput output) {
        super(output, MODID, "en_us");
    }

    private static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    @Override
    protected void addTranslations() {

        ModItems.SPAWN_EGGS.getEntries().forEach(entity -> add(entity.get(), toTitleCase(entity.getId().getPath())));
        ModEffects.EFFECTS.getEntries().forEach(effect -> add(effect.get(), toTitleCase(effect.getId().getPath())));

        add("itemGroup.terraentity.title", "tab.terraentity.name");
        add("message.terraentity.boss_spawn", "%s Has Awoken!");
        add("message.terraentity.boss_leave", "%s Have Been Defeated!");
    }
}
